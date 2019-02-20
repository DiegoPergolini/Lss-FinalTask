
addRule( Rule ):-
	%%output( addRule( Rule ) ),
	assert( Rule ).
removeRule( Rule ):-
	retract( Rule ),
	%%output( removedFact(Rule) ),
	!.
removeRule( A  ):- 
	%%output( remove(A) ),
	retract( A :- B ),!.
removeRule( _  ).

replaceRule( Rule, NewRule ):-
	removeRule( Rule ),addRule( NewRule ).
 
dosomething([]).
dosomething([H|T]) :- addRule(H),dosomething(T).
