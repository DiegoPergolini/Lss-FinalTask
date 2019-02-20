%====================================================================================
% Context ctxRobotRetriever  SYSTEM-configuration: file it.unibo.ctxRobotRetriever.robotWenvExplore.pl 
%====================================================================================
pubsubserveraddr("tcp://localhost:1883").
pubsubsystopic("unibo/qasys").
%%% -------------------------------------------
context(ctxrobotexplore, "localhost",  "TCP", "8028" ).  		 
context(ctxrobotretriever, "localhost",  "TCP", "8100" ).  		 
%%% -------------------------------------------
qactor( temperature_emitter , ctxrobotexplore, "it.unibo.temperature_emitter.MsgHandle_Temperature_emitter"   ). %%store msgs 
qactor( temperature_emitter_ctrl , ctxrobotexplore, "it.unibo.temperature_emitter.Temperature_emitter"   ). %%control-driven 
qactor( ledcontrol , ctxrobotexplore, "it.unibo.ledcontrol.MsgHandle_Ledcontrol"   ). %%store msgs 
qactor( ledcontrol_ctrl , ctxrobotexplore, "it.unibo.ledcontrol.Ledcontrol"   ). %%control-driven 
qactor( map_keeper , ctxrobotretriever, "it.unibo.map_keeper.MsgHandle_Map_keeper"   ). %%store msgs 
qactor( map_keeper_ctrl , ctxrobotretriever, "it.unibo.map_keeper.Map_keeper"   ). %%control-driven 
qactor( player_retriever , ctxrobotretriever, "it.unibo.player_retriever.MsgHandle_Player_retriever"   ). %%store msgs 
qactor( player_retriever_ctrl , ctxrobotretriever, "it.unibo.player_retriever.Player_retriever"   ). %%control-driven 
qactor( robot_retriever , ctxrobotretriever, "it.unibo.robot_retriever.MsgHandle_Robot_retriever"   ). %%store msgs 
qactor( robot_retriever_ctrl , ctxrobotretriever, "it.unibo.robot_retriever.Robot_retriever"   ). %%control-driven 
qactor( worldobserver , ctxrobotexplore, "it.unibo.worldobserver.MsgHandle_Worldobserver"   ). %%store msgs 
qactor( worldobserver_ctrl , ctxrobotexplore, "it.unibo.worldobserver.Worldobserver"   ). %%control-driven 
qactor( cmdrobotconverter , ctxrobotexplore, "it.unibo.cmdrobotconverter.MsgHandle_Cmdrobotconverter"   ). %%store msgs 
qactor( cmdrobotconverter_ctrl , ctxrobotexplore, "it.unibo.cmdrobotconverter.Cmdrobotconverter"   ). %%control-driven 
qactor( player , ctxrobotexplore, "it.unibo.player.MsgHandle_Player"   ). %%store msgs 
qactor( player_ctrl , ctxrobotexplore, "it.unibo.player.Player"   ). %%control-driven 
qactor( onecellforward , ctxrobotexplore, "it.unibo.onecellforward.MsgHandle_Onecellforward"   ). %%store msgs 
qactor( onecellforward_ctrl , ctxrobotexplore, "it.unibo.onecellforward.Onecellforward"   ). %%control-driven 
qactor( mind , ctxrobotexplore, "it.unibo.mind.MsgHandle_Mind"   ). %%store msgs 
qactor( mind_ctrl , ctxrobotexplore, "it.unibo.mind.Mind"   ). %%control-driven 
%%% -------------------------------------------
%%% -------------------------------------------

