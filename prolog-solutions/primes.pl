init(MAX_N) :- sieve(2, MAX_N).

prime(N) :- N \= 1, \+ mulTable(N).
composite(N) :- not(prime(N)).

mulnumber(N, J, MAX_N) :- 
	N =< MAX_N, 
	assert(mulTable(N)), 
	NN is N + J, 
	mulnumber(NN, J, MAX_N).

sieve(N, MAX_N) :-
	K is N * N,
	K =< MAX_N,
	\+mulTable(N),
	NN is 2 * N,
	mulnumber(NN, N, MAX_N).
sieve(N, MAX_N) :-
	N =< MAX_N,
	R is N+1,
	sieve(R, MAX_N).

next_prime(N, A) :- R is N+1, \+prime(R), next_prime(R, M), A is M.
next_prime(N, A) :- R is N+1, prime(R), !, A is R.


pow_k(0,K,TEMP, RES):- RES = TEMP, !.
pow_k(N,K,TEMP, RES):- N1 is div(N, K), TEMP1 is TEMP+1, pow_k(N1, K, TEMP1, RES).

prime_palindrome(N, K):- prime(N), pow_k(N, K, 0, PK), MOD is mod(PK, 2), PK1 is div(PK, 2), check_palindr(N, K, PK1, [], MOD).


check_palindr(0, K, 0, [], 0):-!.
check_palindr(N, K, 0, [H | T], 0):-!, N1 is mod(N, K), N2 is div(N, K), N1 = H, check_palindr(N2, K, 0, T, 0).
check_palindr(N, K, 0, L, 1):-!, N1 is div(N, K), check_palindr(N1, K, 0, L, 0).
check_palindr(N, K, PK, L, F):- N1 is mod(N, K), N2 is div(N, K), Temp = [N1], append(Temp, L, L1), PK1 is PK-1, check_palindr(N2, K, PK1, L1, F).

divisors(1, _, []).
divisors(N, D, [N]) :- prime(N).
divisors(N, D, R1) :- N >= D * D, prime(D), 0 is mod(N, D), M is div(N, D), divisors(M, D, R2), !, append([D | _], R2, R1).
divisors(N, D, R1) :- N >= D * D, \+(0 is mod(N, D)), next_prime(D, DN), divisors(N, DN, R1).

mult(PR, [], R, RES) :- RES is R.
mult(PR, [H | T], R, RES):- PR =< H, prime(H), R1 is H * R, mult(H, T, R1, RES).

prime_divisors(N, R) :- integer(N), divisors(N, 2, R), !.
prime_divisors(N, [H | T]) :- mult(2, [H | T], 1, N1), N is N1, !.
prime_divisors(N, [H]) :- N = H.
prime_divisors(N, []) :- N is 1, !.


