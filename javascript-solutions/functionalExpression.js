"use strict";


function Unary(exp) {
    this.exp = exp;

    Unary.prototype.evaluate = function (x, y, z) {
        return this.calc(x, y, z);
    }
    Unary.prototype.toString = function () {
        return this.exp.toString() + (this.char === undefined ? "" : " " + this.char);
    };
    Unary.prototype.prefix = function (x, y, z) {
        return (this.char === undefined ? this.exp.prefix() : "(" + this.char + " " + this.exp.prefix() + ")");
    };
    Unary.prototype.postfix = function (x, y, z) {
        return (this.char === undefined ? this.exp.postfix() : "("  + this.exp.postfix() +  " "+ this.char +")");
    };
}



function Uop(calc, char) {
    function Operation(first) {
        Unary.call(this, first);
    }

    Operation.prototype = Object.create(Unary.prototype);
    Operation.prototype.char = char;
    Operation.prototype.calc = calc;
    return Operation;
}

function Binary(first, second) {
    this.first = first;
    this.second = second;
    Binary.prototype.evaluate = function (x, y, z) {
        return this.calc(this.first.evaluate(x, y, z), this.second.evaluate(x, y, z));
    };

    Binary.prototype.toString = function () {
        return this.first.toString() + " " + this.second.toString() + " " + this.char;
    };
    Binary.prototype.prefix = function () {
        return "(" + this.char + " " + this.first.prefix() + " " + this.second.prefix() + ")";
    };
    Binary.prototype.postfix = function () {
        return "("+this.first.postfix() + " " + this.second.postfix() +" " + this.char +")";
    };
}
function Multiple(...args) {
    this.args = args;
    Multiple.prototype.evaluate = function (x, y, z) {
        let glob = [];
        for(let i = 0; i < this.args.length;i++){
            glob.push(this.args[i].evaluate(x,y,z));
        }
        return this.calc(glob);
    };

    Multiple.prototype.toString = function () {
        let string = "";
        for(let i = 0; i < this.args.length-1;i++){
            string = string + this.args[i].toString() + " ";
        }
        return string + this.args[this.args.length -1];
    };
    Multiple.prototype.prefix = function () {
        return "(" + this.char + " " + Multiple.prototype.toString() + ")";
    };
    Multiple.prototype.postfix = function () {
        return "(" + Multiple.prototype.toString() + " " + this.char +")";
    };
}
function Mop(calc, char) {
    function Operation(...args) {
        Multiple.call(this, args);
    }
    Operation.prototype = Object.create(Multiple.prototype);
    Operation.prototype.char = char;
    Operation.prototype.calc = calc;
    return Operation;
}

function Bop(calc, char) {
    function Operation(first, second) {
        Binary.call(this, first, second);
    }
    Operation.prototype = Object.create(Binary.prototype);
    Operation.prototype.char = char;
    Operation.prototype.calc = calc;
    return Operation;
}
const Mean = new Mop((...args) => ((args.reduce((previousValue, currentValue) => previousValue + currentValue), 0)/args.length), "mean");
//(args.reduce((previousValue, currentValue) => previousValue + currentValue, 0) / args.length), "mean");
const Var = new Mop((...args) => ((args.reduce(
        (previousValue, currentValue) => previousValue + currentValue*currentValue, 0)-
    args.reduce(
        (previousValue, currentValue) => previousValue + currentValue, 0)) / args.length), "var");
const variables = ["x", "y", "z"];

const Variable = new Uop(function (...values) {
    return values[variables.indexOf(this.exp)]
});

const Const = new Uop(function (x, y, z) {
        return this.exp;
    }
);

const Negate = new Uop(function (x, y, z) {
    return this.exp.evaluate(x, y, z) * -1;
}, "negate");

Const.prototype.diff = function () {
    return new Const(0);
};

Negate.prototype.diff = function (name) {
    return new Negate(this.exp.diff(name));
};

Const.prototype.prefix = function () {
    return this.exp.toString();
}
Variable.prototype.prefix = function () {
    return this.exp.toString();
}
Const.prototype.postfix = function () {
    return this.exp.toString();
}
Variable.prototype.postfix = function () {
    return this.exp.toString();
}
Variable.prototype.diff = function (name) {
    if (name === this.exp)
        return new Const(1);
    else
        return new Const(0);
};
let E = new Const(Math.E);

const Add = new Bop((a, b) => (a + b), "+");
const Subtract = new Bop((a, b) => (a - b), "-");
const Multiply = new Bop((a, b) => (a * b), "*");
const Divide = new Bop((a, b) => (a / b), "/");
const Pow = new Bop((a, b) => (Math.pow(a, b)), "pow");
const Log = new Bop((a, b) => (Math.log(Math.abs(b)) / Math.log(Math.abs(a))), "log");


Add.prototype.diff = function (name) {
    return new Add(this.first.diff(name), this.second.diff(name));
};
Subtract.prototype.diff = function (name) {
    return new Subtract(this.first.diff(name), this.second.diff(name));
};
Multiply.prototype.diff = function (name) {
    return new Add(
        new Multiply(this.first, this.second.diff(name)),
        new Multiply(this.first.diff(name), this.second)
    );
};
Divide.prototype.diff = function (name) {
    return new Divide(
        new Subtract(
            new Multiply(this.first.diff(name), this.second),
            new Multiply(this.first, this.second.diff(name))
        ),
        new Multiply(this.second, this.second)
    );
};
Pow.prototype.diff = function (name) {
    return new Multiply(new Pow(this.first, this.second), new Multiply(this.second, new Log(E, this.first)).diff(name));
};

Log.prototype.diff = function (name) {
    if (this.first === E) {
        return new Multiply(new Divide(new Const(1), this.second), this.second.diff(name));
    }
    return new Divide(new Log(E, this.second), new Log(E, this.first)).diff(name);
};

let operations = new Map([
        ["+", [Add, 2]],
        ["-", [Subtract, 2]],
        ["/", [Divide ,2]],
        ["*", [Multiply, 2]],
        ["negate", [Negate, 1]],
        ["pow", [Pow, 2]],
        ["log", [Log, 2]],
        ["mean", [Mean, Infinity]],
        ["var", [Var, Infinity]]

    ]
);

const parse = expression => {
    let tokens = expression.split(/\s+/);
    let stack = [];
    tokens.map(function(token) {
        if ((token[0]>=0 && token[0]<=9) || (token[0] === '-' && token.length !== 1)) {
            stack.push(new Const(parseInt(token)));
        } else if ((token === "x" || token === "y" || token === "z")) {
            stack.push(new Variable(token));
        }else if (operations.has(token)) {
            let args = [];
            let len = stack.length;
            stack.slice(len - operations.get(token)[1], len).forEach(function () {
                args.push(stack.pop());
            });
            args.reverse()
            stack.push(new (operations.get(token)[0])(...args));
        }


    })

    return stack.pop();
};

function parsePrefix(string) {
    let pos = 0;

    function parser(balance) {
        let op = "empty";
        let args = [];
        while (pos < string.length) {
            while (string[pos] === " ") pos++;
            switch (string[pos]) {
                case "(": {
                    pos++;
                    args.push(parser(balance + 1));
                    break;
                }
                case ")": {
                    if (balance <= 0) throw new Error("Closing parenthesis");
                    pos++;
                    return wrap(op, ...args);
                }
                default: {
                    let token = "";
                    while (pos < string.length && string[pos] !== " " && string[pos] !== ")" && string[pos] !== "(") token += string[pos++];
                    if (token === "x" || token === "y" || token === "z") args.push(new Variable(token));
                    else {
                        if (Number.isInteger(+token)) args.push(new Const(Number(token)));
                        else if (operations.has(token)) {
                            if (op !== "empty") throw new Error(token + " tries to replace " + op);
                            op = token;
                        } else throw new Error(token + "Unknown character");
                    }
                }
            }
            while (string[pos] === " ") pos++;
        }
        if (balance !== 0) throw new Error("Not-zero balance of brackets");
        if (args.length !== 1 || op !== "empty") {
            throw new Error(args + " : " + op + " - Invalid result");
        }
        return args[0];
    }

    function wrap(op, ...args) {
        if (args.length !== operations.get(op)[1] && operations.get(op)[1]!==Infinity) {
            if (operations.get(op)[1] > 1) throw new Error("Error in the number of arguments in BinOp  in \"" + pos +"\"");
            throw new Error("Error in the number of arguments in UnaryOp  in \"" + pos + "\"");
        }
        return new (operations.get(op)[0])(...args);
    }

    return parser(0);
}
function parsePostfix(string) {
    return parsePrefix(string);
}


