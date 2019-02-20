%==============================================
% WorldTheory.pl for actor worldobserver
%==============================================
/*
For a QActor as a singleton statically degined in the model
*/
myname(qatuworldobserver).	%%old version (deprecated)
actorobj(qatuworldobserver).	%% see registerActorInProlog18 in QActor

/*
For a QActor instance of name=Name dynamically created
*/
setActorName( Name ):-
	retract( myname(X) ),
	retract( actorobj(X) ),
	text_term( TS,Name ),
	text_concat("qatu",TS,NN),
	assert( myname(NN) ),
	assert( actorobj(NN) ).
createActor(Name, Class ):-
 	actorobj(A),
 	%% actorPrintln( createActor(A) ),
	A <- getName returns CurActorName,
	%% actorPrintln( createActor(CurActorName,Ctx,View) ),
 	A <- getQActorContext  returns Ctx,
 	%% actorPrintln( createActor(CurActorName,Ctx,View) ),
    A <- getOutputEnvView returns View,
	%% actorPrintln( createActor(CurActorName,Ctx,View) ),
 	Ctx <- addInstance(Ctx, Name,Class,View). 
/*
Name generator
*/	
value(nameCounter,0). 
newName( Prot, Name,N1 ) :-
	inc(nameCounter,1,N1),
	text_term(N1S,N1), 
	text_term(ProtS,Prot),
 	text_concat(ProtS,N1S,Name),
	replaceRule( instance( _, _, _ ), instance( Prot, N1, Name ) ).
newInstance( Prot, Name, P ) :-
 	text_term(N1S,P), 
	text_term(ProtS,Prot),
 	text_concat(ProtS,N1S,Name),
	replaceRule( instance( _, _, _ ), instance( Prot, P, Name ) ).

%%setPrologResult( timeOut(T) ):- setTimeOut( tout("prolog", tout(T))  ).

setPrologResult( Res ):-
	( retract( goalResult( _ ) ),!; true ),	%%remove previous goalResult (if any) 
	assert( goalResult(Res) ).
setTimeOut( tout( EV, A ) ):-
	( retract( tout( _._ ) ),!; true ),		    %%remove previous tout (if any) 
	assert( tout( EV, A ) ).

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
	
setResult( A ):-
 	( retract( result( _ ) ),!; true ), %%remove previous result (if any)	
	assert( result( A ) ).

evalGuard( not(G) ) :-
	G, !, fail .
evalGuard( not(G) ):- !.
evalGuard( true ) :- !.
evalGuard( G ) :-  
	%stdout <- println( evalGuard( G ) ),
	G . 

output( M ):-stdout <- println( M ).
%-------------------------------------------------
%  TuProlo FEATURES of the QActor worldobserver
%-------------------------------------------------
dialog( FileName ) :-  
	java_object('javax.swing.JFileChooser', [], Dialog),
	Dialog <- showOpenDialog(_),
	Dialog <- getSelectedFile returns File,
	File <- getName returns FileName. 		 

%% :- stdout <- println(  "hello from world theory of worldobserver" ). 

%-------------------------------------------------
%  UTILITIES for TuProlog computations
%-------------------------------------------------
loop( N,Op ) :- 
	assign(loopcount,1),
	loop(loopcount,N,Op).

loop(I,N,Op) :-  
		value(I,V),
		%actorPrintln( values( I,V,N,Op ) ),
 		V =< N ,!,  
   		%actorPrintln( loop( I,V ) ),
      	actorOp(  Op  ) ,
		V1 is V + 1 ,
		assign(I,V1) ,   
		loop(I,N,Op).	%%tail recursion
loop(I,N,Op).

numOfFacts( F, N ) :-
	bagof( F,F,L ),
	length(L, N). 
	
eval( plus, V1, V2, R  ):- R is V1+V2.
eval( minus, V1, V2, R ):- R is V1-V2.
eval( times, V1, V2, R ):- R is V1*V2.
eval( div, V1, V2, R   ):- R is V1/V2.
eval( lt, X,Y ) :- X < Y.
eval( gt, X,Y ) :- X > Y.
eval( le, X,Y ) :- X =< Y.
eval( eq, X, X ).


divisible( V1, V2 ) :- 0 is mod(V1,V2). 

getVal( I, V ):-
	value(I,V), !.
getVal( I, failure ).

assign( I,V ):-
	retract( value(I,_) ),!,
	assert( value( I,V )).
assign( I,V ):-
	assert( value( I,V )).

inc(I,K,N):-
	value( I,V ),
	N is V + K,
	assign( I,N ).
dec(I,K,N):-
	value( I,V ),
	N is V - K,
	assign( I,N ).
	
actorPrintln( X ):- actorobj(A), text_term(XS,X), A  <- println( XS ).

%-------------------------------------------------
%  User static rules about worldobserver
%------------------------------------------------- 
temperatureState( 0).
robotState( stopped).
ledState( off).
worldState( T,S):-temperatureState( T),robotState( S).
invalidCond( T):-temperatureState( T),eval( gt,T,25).
limitTemperatureValue( 25).
changeTemperatureState( T):-replaceRule( temperatureState( _),temperatureState( T)),invalidCond( T),output( invalidCond( T)).
changeRobotState( exploring):-replaceRule( robotState( _),robotState( exploring)),replaceRule( ledState( _),ledState( blinking)),robotState( T),ledState( S),output( S).
changeRobotState( stopped):-replaceRule( robotState( _),robotState( stopped)),replaceRule( ledState( _),ledState( off)),robotState( T),ledState( S),output( S).
changeRobotState( waitingSecondRobot):-replaceRule( robotState( _),robotState( waitingSecondRobot)),replaceRule( ledState( _),ledState( off)),robotState( T),ledState( S),output( S).
changeRobotState( readyToStart):-replaceRule( robotState( _),robotState( readyToStart)),replaceRule( ledState( _),ledState( off)),robotState( T),ledState( S),output( S).
changeRobotState( waitingObstacleClassification):-replaceRule( robotState( _),robotState( waitingObstacleClassification)),replaceRule( ledState( _),ledState( blinking)),robotState( T),ledState( S),output( S).
changeRobotState( comingBackToHomeSinceBomb):-replaceRule( robotState( _),robotState( comingBackToHomeSinceBomb)),replaceRule( ledState( _),ledState( blinking)),robotState( T),ledState( S),output( S).
changeRobotState( comingBackToHomeSinceHalt):-replaceRule( robotState( _),robotState( comingBackToHomeSinceHalt)),replaceRule( ledState( _),ledState( off)),robotState( T),ledState( S),output( S).
changeRobotState( atHomeWithInvalidCondition):-replaceRule( robotState( _),robotState( atHomeWithInvalidCondition)),replaceRule( ledState( _),ledState( off)),robotState( T),ledState( S),output( S).
changeRobotState( comingBackToHomeSinceInvalidCondition):-replaceRule( robotState( _),robotState( comingBackToHomeSinceInvalidCondition)),replaceRule( ledState( _),ledState( off)),robotState( T),ledState( S),output( S).
isCellToUpdate( X,Y,S):-cellState( X,Y,Z),output( Y),replaceRule( cellState( X,Y,Z),cellState( X,Y,S)).
allCell( D):-findall( cellState( X,Y,S),cellState( X,Y,S),D).
/*
------------------------------------------------------------------------
testex :- actorPrintln( testex ),
java_catch(
	java_object('Counter', ['MyCounter'], c),
	%% java_object('java.util.ArrayList', [], l),
	[ (EEE,              actorPrintln( a )) ],
	  %% ('java.lang.Exception'(Cause, Message, _),              actorPrintln( b )),
	  %% ('java.lang.ClassNotFoundException'(Cause, Message, _), actorPrintln( c ))  ],
	actorPrintln( d )
).
------------------------------------------------------------------------
*/
loadTheory(T):-
 	%% actorPrintln( loadTheory(T) ),
	consult(T), !.
loadTheory(T):-
	actorPrintln( loadTheory(T, FAILURE) ).

opInfo( Op,F,A,L ):-
	functor( Op, F, A ),
	Op =.. L.
		
actorOp( Op )   :- actorobj(Actor),
				   % actorPrintln( actorOp( Op  ) ),
				   java_catch(
						Actor <- Op returns R, 	%% R unbound if void
 						[ ( E,  output( actorOpFailure( E  ) ), setActorOpResult( Op,failure ), fail )],  				
  						setActorOpResult( Op,R )				%%executed in any way
					). 
actionResultOp( Op ):-
					% actorPrintln( actionResultOp( Op  ) ),
				   java_catch(
				   		%% actoropresult is bound to actorOp result (registered by Actor<-setActionResult)
						actoropresult <- Op returns R, 	%% R unbound if void
 						[ ( E, setActorOpResult( Op,failure ), fail )],  
  						setActorOpResult( Op,R )				%%executed in any way
					). 

%% setActorOpResulttt(Op, V ):-  actorPrintln( setActorOpResulttt(Op, V ) ),setActorOpResult( Op, V ).
setActorOpResult( Op, null ):-   !, storeActorOpResult( Op,void  ).
setActorOpResult( Op, V ):-   unbound(V),!, storeActorOpResult( Op,void  ).	  
setActorOpResult( Op, Res ):- text_term(Res,Term),!,storeActorOpResult( Op,Term  ).
setActorOpResult( Op, Res ):- %% Res is $obj_xxx	
	cvtToString(Res,ResStr),
	%% actorPrintln( actorOpDone( Op,ResStr  ) ),  
   	storeActorOpResult( Op, ResStr ).
storeActorOpResult( Op, R ):-
	%% actorPrintln( storeActorOpResult( Op,R  ) ),  
	( retract( actorOpDone( _,_ ) ),!; true ), %%remove previous actionResult (if any) 
	assert( actorOpDone( Op, R) ).


%% setActionResult : Actor register (in Java) the result under name 'actoropresult'
cvtToString( true , "true" ):-  %% true is the result of robot move operation
	setResult(true).
cvtToString( false , "false" ):-   
	setResult(false).
cvtToString( V , "void" ):-  
	unbound(V),!,
	actorobj(Actor),
	Actor <-  setActionResult( void ).
cvtToString( V , S ):-
	number(V),!,
	setResult(V),
	text_term( S, V ).
cvtToString( V , S ):-
	( retract( result(_) ),!; true ),		%eliminate any previous numeric result
	V <- toString returns S,
	actorobj(Actor),
	Actor <- setActionResult( V ),
	!.
actorOpResult( V )   :- result(V),!.	
actorOpResult( Res ) :- actoropresult <- toString returns Res, !.
%% actorOpResult( "" ).

androidConsult(T) :- actorobj(Actor), Actor <- androidConsult(T), !.
androidConsult(T) :- actorPrintln( failure(androidConsult) ).



/*
%-------------------------------------------------
%  Some predefined code
%------------------------------------------------- 
*/
fibo(0,1).
fibo(1,1).
fibo(2,1).
fibo(3,2).
fibo(4,3).
fibo(I,N) :- I < 5, !, fail.	%%%JULY2017, if N bound
fibo(I,N) :- V1 is I-1, V2 is I-2,
  fibo(V1,N1), fibo(V2,N2),
  N is N1 + N2.

%% Fibonacci with cache (to be used in guards)

fib(V):-
	fibmemo( V,N ),!,
	actorPrintln( fib_a(V,N) ).
fib(V):-
	fibWithCache(V,N), 					
	actorPrintln( fib_b(V,N) ).

fib( V,R ) :-
	fibWithCache(V,R).

fibmemo( 0,1 ).
fibmemo( 1,1 ).
fibmemo( 2,1 ).
fibmemo( 3,2 ).
fibWithCache( V,N ) :-
	fibmemo( V,N ),!.
fibWithCache( V,N ) :- V < 4, !, fail. %%%JULY2017, if N bound
fibWithCache( V,N ) :-
	V1 is V-1, V2 is V-2,
  	fibWithCache(V1,N1), fibWithCache(V2,N2),
  	N is N1 + N2,
	%% actorPrintln( fib( V,N ) ),
	assert( fibmemo( V,N ) ).



/*
DEC2018
*/
getTheContexts(Ctx,CTXS) :-
 	Ctx <- solvegoal("getTheContexts(X)","X") returns CTXS .
getTheActors(Ctx,ACTORS) :-
 	Ctx <- solvegoal("getTheActors(X)","X") returns ACTORS.
 
 /*
 ----------------------------------
Show system configuration
 ----------------------------------
 */	
showSystemConfiguration :-
	actorPrintln('------------------Qa---------------------------'),
	actorobj(A),
	A <- getQActorContext returns Ctx,
   	%% Ctx <- getName returns CtxName, actorPrintln( CtxName ), 	 
 	getTheContexts(Ctx,CTXS),
	actorPrintln('CONTEXTS IN THE SYSTEM:'),
	showElements(CTXS),
	actorPrintln('ACTORS   IN THE SYSTEM:'),
	getTheActors(Ctx,ACTORS),
	showElements(ACTORS),
	actorPrintln('------------------Qa---------------------------').
 
showElements(ElementListString):- 
	text_term( ElementListString, ElementList ),
	%% actorPrintln( list(ElementList) ),
	showListOfElements(ElementList).
showListOfElements([]).
showListOfElements([C|R]):-
	actorPrintln( C ),
	showElements(R).

/*
------------------------------------------------------------
initialize
------------------------------------------------------------
*/
initialize  :-  
	actorobj(Actor),
 	output( worlTheoryLoaded(Actor) ).
 
%% :- initialization(initialize).
