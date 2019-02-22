var pl = require( "../../node_modules/tau-prolog/modules/core" );

module.exports = class QAmsg {

    constructor(msg){
        var session = pl.create( 1000 );
        session.consult(msg+".");
        session.query( "msg(I,T,S,R,C,N).");
        session.answer(x=>{
            if(pl.type.is_substitution(x)){
                this.msgId  = x.lookup("I").toJavaScript();
                this.msgType = x.lookup("T").toJavaScript();
                this.sender = x.lookup("S").toJavaScript();
                this.receiver = x.lookup("R").toJavaScript();
                this.content = x.lookup("C").toString();
                this.seqNum = x.lookup("N").toJavaScript();
            }
        });
    }
    toCompactForm(){
        return "msg("+this.msgId+","+this.msgType+","+this.sender+","+this.receiver+","+this.content+","+this.seqNum+")"
    }
    testUnification(goal){
        var session = pl.create( 1000 );
        session.consult(this.content+".");
        goal = goal+".";
        console.log(goal);
        session.query(goal);
        var result;
        session.answer(x => result= pl.type.is_substitution(x));
        return result;
    }
    unifyWithContent(){
        var session = pl.create( 1000 );
        session.consult(this.content+".");
        const goal = arguments[0]+".";
        session.query(goal);
        var result = {};
        session.answer(x=>{
            if(pl.type.is_substitution(x)){
                for(var i = 1;i<arguments.length;i++){
                    result[arguments[i]] = x.lookup(arguments[i]).toJavaScript();
                }
            }
        });
        return result;
    }
};