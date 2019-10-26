package com.etas

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContextExecutor

trait AkkaCoreModule {
  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val materilizer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher
}
