package com.loudbook.dev

import net.md_5.bungee.api.plugin.Plugin

class LoudsProxy : Plugin() {
    private lateinit var redis: Redis
    override fun onEnable() {
        val redis = Redis()
        redis.subscribeServerSend()
    }

    override fun onDisable() {
        redis.client.shutdown()
    }
}