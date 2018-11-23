/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicejuridique.sources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author marieroca
 */
public class DateConvention {
    private Date date;
    final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    static final long DUREE_UN_JOUR = 24 * 60 * 60 * 1000;
    /*//Si on veut prendre en compte différents formats de dates
        String s = "";
        if (dateEntree.contains("/"))
        s = "/";
        if (dateEntree.contains("-"))
        s = "-";
        if (dateEntree.contains("."))
        s = ".";
        String date[] = dateEntree.split(s);
        //et on split*/

    /**
     * Constructeur qui créé une date propre à l'application à partir de aujourd'hui
     */
    public DateConvention() {
        date = new Date();
    }

    /**
     * Constructeur qui créé une date propre à l'application à partir d'une chaine de caractère passée en paramètre
     * @param date
     *          chaine de caractère représentant la date "jj/mm/aaaa"
     * @throws java.text.ParseException
     *          exception soulevée si la chaine de caractère ne permet pas de créer la date
     */
    public DateConvention(String date) throws ParseException{
        this.date = (Date) FORMAT.parse(date);
    }
    
    /**
     * Constructeur qui créé une date propre à l'entreprise à partir d'une date
     * @param date 
     *          date (type java.util.Date) permettant de créer la date entreprise
     */
    public DateConvention(Date date) {
        this.date = date;
    }
    
    /**
     * Calcule le nombre de jours entre deux dates
     * @param dateDeb
     *          Date de départ
     * @param dateFin
     *          Date de fin
     * @return le nombre de jours entre deux dates
     */
    public static int getNbJours(Date dateDeb, Date dateFin){
        long diff = Math.abs(dateDeb.getTime() - dateFin.getTime());
        long nbJours = (long)diff/DUREE_UN_JOUR;
        return (int)nbJours;
    }
    
    public static int nbMois(Date dateDeb, Date dateFin){
        if(dateFin.getDay()>=dateDeb.getDay())
            return dateFin.getMonth()- dateDeb.getMonth();
        else if(dateFin.getMonth() < dateDeb.getMonth())
            return dateFin.getMonth() - dateDeb.getMonth() - 1;
        return 0;
    }
    
    public static int nbJours(Date dateDeb, Date dateFin){
        if(dateFin.getDay() >= dateDeb.getDay())
            return dateFin.getDay() - dateDeb.getDay();
        else
            switch(dateFin.getMonth()){
                case 2 :
                case 4 :
                case 6 :
                case 9 :
                case 11 :
                    return 31 - dateDeb.getDay() + dateFin.getDay();
                case 3 :
                    return 29 - dateDeb.getDay() + dateFin.getDay();
                default :
                    return 30 - dateDeb.getDay() + dateFin.getDay();
            }
    }
    
    @Override
    public String toString(){
        return FORMAT.format(date);
    }
    
    /**
     * Getter sur la date courante
     * @return la date courante
     */
    public Date getDate(){
        return date;
    } 
    
}
