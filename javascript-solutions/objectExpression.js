"use strict";


function Multiple(...args) {
    this.args = args;
    this.evaluate = function (x, y, z) {
        return this.calc(...this.args.map(arg => arg.evaluate(x, y, z)))
    };
    this.toString = function () {
        return this.args.reduce(
            (curr, a) => curr + a.toString() + " ",
            "") + this.char;
    };
    this.prefix = function () {
        return "(" + this.char + this.args.reduce(
            (curr, a) => curr + " " + a.prefix(),
            "") + ")";
    };
    this.postfix = function () {
        return "(" + this.args.reduce(
            (curr, a) => curr + a.postfix() + " ",
            "") + this.char + ")";
    };

    this.diff = function (variable) {
        return this.derivative(variable, ...this.args)
    };
}

function Opera(Operation, calc, derivative, char, quantity) {

    Operation.prototype = Object.create(Multiple.prototype);
    Operation.prototype.char = char;
    Operation.prototype.calc = calc;
    Operation.prototype.derivative = derivative;

    Operation.quantity = quantity;


    return Operation;
}

function Mop(calc, diff, char, quantity) {
    function Operation(...args) {
        Multiple.call(this, ...args)
    }

    return new Opera(Operation, calc, diff, char, quantity);

}

function Uop(calc, diff, char, quantity) {
    function Operation(first) {
        Multiple.call(this, first)
    }

    return new Opera(Operation, calc, diff, char, quantity);
}

function Bop(calc, diff, char, quantity) {
    function Operation(first, second) {
        Multiple.call(this, first, second)
    }
    return new Opera(Operation, calc, diff, char, quantity);
}


const mean = function (f, ...args) {
    return args.reduce(
        (previousValue, currentValue) => previousValue + f(currentValue), 0) / args.length
}

const Mean = new Mop((...args) => mean(a => a, ...args),
    (variable, ...args) => new Mean(...args.map(arg => arg.diff(variable))),
    "mean", Infinity);

const Var = new Mop((...args) => (mean(a => a * a, ...args) - mean(a => a, ...args) ** 2),
    (variable, ...args) => new Subtract(
        new Mean(...args.map(arg => (new Pow(arg, new Const(2))))),
        new Pow(new Mean(...args), new Const(2))
    ).diff(variable),
    "var", Infinity);


const Negate = new Uop(a => -a,
    (name, first) => new Negate(first.diff(name)),
    "negate", 1);


const Add = new Bop((a, b) => (a + b),
    (name, first, second) => new Add(first.diff(name), second.diff(name)),
    "+", 2);

const Subtract = new Bop((a, b) => (a - b),
    (name, first, second) => new Subtract(first.diff(name), second.diff(name)),
    "-", 2);

const Multiply = new Bop((a, b) => (a * b),
    (name, first, second) => {
        return new Add(
            new Multiply(first, second.diff(name)),
            new Multiply(first.diff(name), second)
        );
    },
    "*", 2);

const Divide = new Bop((a, b) => (a / b),
    (name, first, second) => {
        return new Divide(
            new Subtract(
                new Multiply(first.diff(name), second),
                new Multiply(first, second.diff(name))
            ),
            new Multiply(second, second)
        );
    },
    "/", 2);

const Pow = new Bop((a, b) => (Math.pow(a, b)),
    (name, first, second) => {
        return new Multiply(new Pow(first, second),
            new Multiply(second, new Log(E, first)).diff(name));
    },
    "pow", 2);

const Log = new Bop((a, b) => (Math.log(Math.abs(b)) / Math.log(Math.abs(a))),
    (name, first, second) => {
        if (first === E) {
            return new Multiply(new Divide(new Const(1), second), second.diff(name));
        }
        return new Divide(new Log(E, second), new Log(E, first)).diff(name);
    },
    "log", 2);

const variables = ["x", "y", "z"];

function Variable(name) {
    this.name = name;
    this.evaluate = (...values) => values[variables.indexOf(this.name)];
    this.toString = () => this.name.toString();

    this.prefix = () => this.name.toString();
    this.postfix = () => this.name.toString();
    this.diff = function (name) {
        if (name === this.name)
            return new Const(1);
        else
            return new Const(0);
    };
    this.quantity = 1
}

function Const(value) {
    this.value = value;
    this.evaluate = (...values) => value;
    this.toString = () => this.value.toString();
    this.prefix = () => this.value.toString();
    this.postfix = () => this.value.toString();
    this.diff = () => new Const(0);
    this.quantity = 1
}

const E = new Const(Math.E);


let operations = new Map([
        ["+", Add],
        ["-", Subtract],
        ["/", Divide],
        ["*", Multiply],
        ["negate", Negate],
        ["pow", Pow],
        ["log", Log],
        ["mean", Mean],
        ["var", Var]

    ]
);

function ParserError(message) {
    this.name = "ParserError";
    this.message = message;
}

ParserError.prototype = Error.prototype;

const parse = expression => {
    let tokens = expression.split(/\s+/);
    let stack = [];
    tokens.map(function (token) {
        if ((token[0] >= 0 && token[0] <= 9) || (token[0] === '-' && token.length !== 1)) {
            stack.push(new Const(parseInt(token)));
        } else if ((token === "x" || token === "y" || token === "z")) {
            stack.push(new Variable(token));
        } else if (operations.has(token)) {
            let args = [];
            let len = stack.length;
            stack.slice(len - operations.get(token).quantity, len).forEach(function () {
                args.push(stack.pop());
            });
            args.reverse()
            stack.push(new (operations.get(token))(...args));
        }
    })
    return stack.pop();
};


function supportParser(string, isPostfix){
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
                    if (balance <= 0) throw new ParserError("Closing parenthesis");
                    pos++;
                    return wrap(op, ...args);
                }
                default: {
                    let token = "";
                    while (pos < string.length && string[pos] !== " " && string[pos] !== ")" && string[pos] !== "(") token += string[pos++];
                    if (token === "x" || token === "y" || token === "z") args.push(new Variable(token));
                    else {
                        if (Number.isInteger(+token))  {
                            args.push(new Const(Number(token)));
                        } else if (operations.has(token)) {
                            if (op !== "empty") {
                                throw new ParserError(token + " tries to replace " + op);
                            }
                            if(isPostfix) {
                                if(args.length !== operations.get(token).quantity &&
                                    operations.get(token).quantity !== Infinity) {
                                    throw new ParserError("Prefix form used in Postfix");
                                }
                            } else {
                                if(args.length !== 0) {
                                    throw new ParserError("Postfix form used in Prefix");
                                }
                            }
                            op = token;
                        } else {
                            throw new ParserError(token + " Unknown character");
                        }
                    }
                }
            }
            while (string[pos] === " ") pos++;
        }
        if (balance !== 0) throw new ParserError("Not-zero balance of brackets");
        if (args.length !== 1 || op !== "empty") {
            throw new ParserError(args + " - arguments of \"" + op + "\" - Invalid result");
        }
        return args[0];
    }

    function wrap(op, ...args) {
        if (operations.has(op)) {
            if (args.length !== operations.get(op).quantity && operations.get(op).quantity !== Infinity) {
                if (operations.get(op).quantity > 1) throw new ParserError("Error in the number of arguments in BinOp  in \"" + pos + "\"");
                throw new ParserError("Error in the number of arguments in UnaryOp  in \"" + pos + "\"");
            }
            return new (operations.get(op))(...args);
        }
        throw new ParserError("Invalid operation or operation doesnt exist: " + op + " operation");
    }

    return parser(0);
}

function parsePrefix(string) {
    return supportParser(string, false);
}

function parsePostfix(string) {
    return supportParser(string, true);
}




