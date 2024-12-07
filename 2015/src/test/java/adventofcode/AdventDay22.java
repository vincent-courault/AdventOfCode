package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay22 extends Commun {

    static boolean level2;
    static int min ;

    private static void boss(int bossHit, int bossDamage, int playerHit, int playermana, int manaSpent, int shieldCounter, int poisonCounter, int rechargeCounter) {
        if (playermana < 0 || manaSpent > min) {
            return;
        }
        int playerShield = shieldCounter > 0 ? 7 : 0;
        if (poisonCounter > 0) bossHit -= 3;
        if (rechargeCounter > 0) playermana += 101;
        shieldCounter = Math.max(0, shieldCounter - 1);
        poisonCounter = Math.max(0, poisonCounter - 1);
        rechargeCounter = Math.max(0, rechargeCounter - 1);

        if (bossHit <= 0) {
            if (manaSpent < min) min = manaSpent;
            return;
        }
        player(bossHit, bossDamage, playerHit - Math.max(1, bossDamage - playerShield), playermana, manaSpent, shieldCounter, poisonCounter, rechargeCounter);
    }

    private static void player(int bossHit, int bossDamage, int playerHit,
                               int playerMana, int manaSpent, int shieldCounter, 
                               int poisonCounter, int rechargeCounter) {
        if (level2) {
            playerHit -= 1;
        }
        if (playerHit <= 0 || playerMana < 0 || manaSpent > min) {
            return;
        }
        if (poisonCounter > 0) {
            bossHit -= 3;
        }
        if (rechargeCounter > 0) {
            playerMana += 101;
        }
        shieldCounter = Math.max(0, shieldCounter - 1);
        poisonCounter = Math.max(0, poisonCounter - 1);
        rechargeCounter = Math.max(0, rechargeCounter - 1);

        if (playerMana >= 53)
            boss(bossHit - 4, bossDamage, playerHit, playerMana - 53, manaSpent + 53, shieldCounter, poisonCounter, rechargeCounter);

        if (playerMana >= 73)
            boss(bossHit - 2, bossDamage, playerHit + 2, playerMana - 73, manaSpent + 73, shieldCounter, poisonCounter, rechargeCounter);

        if (shieldCounter == 0 && playerMana >= 113)
            boss(bossHit, bossDamage, playerHit, playerMana - 113, manaSpent + 113, 6, poisonCounter, rechargeCounter);

        if (poisonCounter == 0 && playerMana >= 173)
            boss(bossHit, bossDamage, playerHit, playerMana - 173, manaSpent + 173, shieldCounter, 6, rechargeCounter);

        if (rechargeCounter == 0 && playerMana >= 229)
            boss(bossHit, bossDamage, playerHit, playerMana - 229, manaSpent + 229, shieldCounter, poisonCounter, 5);
    }


    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1824, traitement(inputs,false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1937, traitement(inputs,true));
    }

    public int traitement(List<String> inputs,boolean etape2) {
        level2=etape2;
        min=2000;
        int resultat;
        int bossHit = Integer.parseInt(inputs.get(0).split(":")[1].trim());
        int bossDamage = Integer.parseInt(inputs.get(1).split(":")[1].trim());

        player(bossHit, bossDamage, 50, 500, 0, 0, 0, 0);
        resultat = min;
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
