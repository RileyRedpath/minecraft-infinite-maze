package com.riley.maze;

import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Riley on 10/12/2017.
 */
public class MobSpawnListener {
    @SubscribeEvent
    public void CheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        if(event.getY() > 18){
            event.setResult(Event.Result.DENY);
        }
    }
}
