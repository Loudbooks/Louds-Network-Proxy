package com.loudbook.dev

import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.config.ServerInfo
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import java.io.FileInputStream
import java.io.IOException
import java.util.*

class Redis {
    val client: RedissonClient
    private var uri: String? = null

    init {
        try {
            FileInputStream("./plugins/config.properties").use { input ->
                val prop = Properties()
                prop.load(input)
                this.uri = prop.getProperty("uri")
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        val config = Config()
        config.useSingleServer()
            .address = uri
        this.client = Redisson.create(config)
    }

    fun subscribeServerSend() {
        this.client.getTopic("server-send").addListener(String::class.java) { _, msg ->
            val split = msg.split(":")
            val player = split[0]
            val type = split[1]
            val servers = mutableListOf<ServerInfo>()
            for (server in ProxyServer.getInstance().servers) {
                if (server.value.name.contains(type, true)) servers.add(server.value)
            }
            ProxyServer.getInstance().getPlayer(player)?.connect(servers.random())
        }
    }
}