public class PlayerStats {
    static int maxHealth = 100, currentHealth = 100, healthRegen = 0, bulletDamage = 5, money = 0;
    static double currentHealthPercentage = ((double) currentHealth /maxHealth)*100;
    static long lastTime = System.nanoTime();
    static void updateStats(){
        currentHealthPercentage = ((double) currentHealth /maxHealth)*100;
        if (System.nanoTime() - lastTime >= 1000000000){
            if (currentHealth < maxHealth){
                if ((currentHealth + healthRegen) > maxHealth){
                    currentHealth = maxHealth;
                }else {
                    currentHealth+=healthRegen;
                    System.out.println("health regened: " + healthRegen);
                }
            }
            lastTime = System.nanoTime();
        }
        //System.out.println(currentHealthPercentage + " : " + currentHealth + " : " + currentHealth/maxHealth + " : " + maxHealth);
    }

}
