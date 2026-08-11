package net.enigma.windyhills.world.wind;

import net.enigma.windyhills.biome.WindyBiomes;
import net.enigma.windyhills.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public final class WindEffects {

    private WindEffects() {}

    public static void tick(ServerLevel level) {
        WindManager.tick();
        for (ServerPlayer player : level.players()) {
            BlockPos playerPos = player.blockPosition();

            if (!level.getBiome(playerPos).is(WindyBiomes.WINDY_HIGHLANDS) || !level.canSeeSky(player.blockPosition())){
                continue;
            }

            player.addEffect(new MobEffectInstance(MobEffects.SPEED, 40, 1, true, false));
            player.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, 40, 1, true, false));

            var wind = WindManager.getWindVector();

            float volume = (float)(0.16F + WindManager.getStrength()*1.4);

            if(player.getRandom().nextInt(200) == 0){
                level.playSound(null, player.blockPosition(), ModSounds.WIND, SoundSource.AMBIENT, volume, 0.8F + level.getRandom().nextFloat()*0.4F);
            }

            if(player.fallDistance>3){
                player.fallDistance*=0.95F;
            }

            double heightFactor = (playerPos.getY()<280)?Math.max(0.0, playerPos.getY()-100)/100.0 : 0;
            double strength = WindManager.getStrength()*(1.0+heightFactor);
            double strengthy = (Math.random()>0.25)?(WindManager.getStrength()*(Math.random()+1.0)):0;

            if (!(level.getRandom().nextInt(3) == 0)){
                if(player.onGround()){
                    player.setDeltaMovement(player.getDeltaMovement().add(wind.x * strength, 0, wind.z * strength));
                    player.hurtMarked = true;
                    continue;
                }

                player.setDeltaMovement(player.getDeltaMovement().add(wind.x * strength * 0.5, strengthy, wind.z * strength * 0.5));
                player.hurtMarked = true;
                continue;
            }

            for (int i = 0; i < 4; i++) {
                double x = player.getX() + level.getRandom().nextInt(33) - 16;
                double z = player.getZ() + level.getRandom().nextInt(33) - 16;

                level.sendParticles(ParticleTypes.CLOUD, x, player.getY() + 0.3, z, 1, wind.x * 0.15, 0.01, wind.z * 0.15, 0.0);
            }
        }
    }
}