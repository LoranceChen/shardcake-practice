package lorance.practice

import com.devsisters.shardcake._
import com.devsisters.shardcake.interfaces._
import GuildBehaviour._
import zio._

object CakeMain extends ZIOAppDefault {
  def run: Task[Unit] =
    ZIO
      .scoped(program)
      .provide(
        ZLayer.succeed(Config.default),
        ZLayer.succeed(GrpcConfig.default),
        Serialization.javaSerialization, // use java serialization for messages
        Storage.memory, // store data in memory
        ShardManagerClient.liveWithSttp, // client to communicate with the Shard Manager
        GrpcPods.live, // use gRPC protocol
        GrpcShardingService.live, // expose gRPC service
        Sharding.live // sharding logic
      )

}
