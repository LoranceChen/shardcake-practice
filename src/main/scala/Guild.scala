package lorance.practice

import com.devsisters.shardcake.{EntityType, Sharding}
import com.devsisters.shardcake.Messenger.Replier
import GuildMessage.Join
import zio.{Dequeue, RIO, Ref}

import scala.util.{Failure, Success, Try}

sealed trait GuildMessage

object GuildMessage {
  case class Join(userId: String, replier: Replier[Try[Set[String]]])
      extends GuildMessage
  case class Leave(userId: String) extends GuildMessage
}

object Guild extends EntityType[GuildMessage]("guild")

object GuildBehaviour {
  def behavior(entityId: String,
               messages: Dequeue[GuildMessage]): RIO[Sharding, Nothing] = {
    val rst = Ref
      .make(Set.empty[String])
      .flatMap(state => messages.take.flatMap(handleMessage(state, _)).forever)
    rst
  }

  def handleMessage(state: Ref[Set[String]],
                    message: GuildMessage): RIO[Sharding, Unit] =
    message match {
      case GuildMessage.Join(userId, replier) =>
        state.get.flatMap(
          members =>
            if (members.size >= 5)
              replier.reply(Failure(new Exception("Guild is already full!")))
            else
              state.updateAndGet(_ + userId).flatMap { newMembers =>
                replier.reply(Success(newMembers))
            }
        )
      case GuildMessage.Leave(userId) =>
        state.update(_ - userId)
    }

  lazy val program =
    for {
      _ <- Sharding.registerEntity(Guild, behavior)
      _ <- Sharding.registerScoped
      guild <- Sharding.messenger(Guild)
      _ <- guild.send("guild1")(Join("user1", _)).debug
      _ <- guild.send("guild1")(Join("user2", _)).debug
      _ <- guild.send("guild1")(Join("user3", _)).debug
      _ <- guild.send("guild1")(Join("user4", _)).debug
      _ <- guild.send("guild1")(Join("user5", _)).debug
      _ <- guild.send("guild1")(Join("user6", _)).debug
    } yield ()
}
