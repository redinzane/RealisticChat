package ch.k42.aftermath.radiotower;

import org.bukkit.ChatColor;

import java.util.Random;

/**
 * Created by Thomas on 07.02.14.
 */
public class Minions {

    private static final char obfuscate0 = ChatColor.WHITE.getChar();
    private static final char obfuscate1 = ChatColor.GRAY.getChar();
    private static final char obfuscate2 = ChatColor.DARK_GRAY.getChar();

    private static final void appendLvl0(StringBuffer buf, char c){
        buf.append(ChatColor.COLOR_CHAR).append(obfuscate0).append(c);
    }
    private static final void appendLvl1(StringBuffer buf, char c){
        buf.append(ChatColor.COLOR_CHAR).append(obfuscate1).append(c);
    }
    private static final void appendLvl2(StringBuffer buf, char c){
        buf.append(ChatColor.COLOR_CHAR).append(obfuscate2).append(c);
    }
    private static final void appendLvl3(StringBuffer buf, char c){
        buf.append(' ');
    }


    /**
     * Obfuscates a message depending on a scale
     * @param message message to obfuscate
     * @param scale a scale from 0 (nearly no obfuscation) to 1 (nearly fully obfuscated), higher values are possible
     * @return an obfuscated message
     */
    public static final String obfuscateMessage(String message,double scale){
        if(scale>=1.5) return "";
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        double mean = 3.5*scale;
        double sigma = 0.5;

        for(int i=0;i<message.length();i++){
            int obsfucation =0;

            obsfucation = (int) (r.nextGaussian()*sigma+mean);
            if(obsfucation<0)
                obsfucation =0;
            else if (obsfucation>3)
                obsfucation=3;

            switch (obsfucation){
                case 0:
                    appendLvl0(sb,message.charAt(i));
                    break;
                case 1:
                    appendLvl1(sb,message.charAt(i));
                    break;
                case 2:
                    appendLvl2(sb,message.charAt(i));
                    break;
                case 3:
                    appendLvl3(sb,message.charAt(i));
                    break;
            }
        }
        return sb.toString();
    }
}
