/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicejuridique.sources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marieroca
 */

    
public class Etudiant implements Serializable {

    //private static final long serialVersionUID = 1L;

    private Long id;
    protected String nom, prenom, assurance, contrat;
    protected Diplome diplome;
    protected List<PreConvention> preConventions = new ArrayList<>();

    public Etudiant(Long id, String nom, String prenom, String assurance, String contrat, Diplome diplome) {
        this.nom = nom;
        this.prenom = prenom;
        this.assurance = assurance;
        this.contrat = contrat;
        this.diplome = diplome;
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAssurance() {
        return assurance;
    }

    public void setAssurance(String assurance) {
        this.assurance = assurance;
    }

    public String getContrat() {
        return contrat;
    }

    public void setContrat(String contrat) {
        this.contrat = contrat;
    }

    public Diplome getDiplome() {
        return diplome;
    }

    public void setDiplome(Diplome diplome) {
        this.diplome = diplome;
    }

    @Override
    /*public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Etudiant)) {
            return false;
        }
        Etudiant other = (Etudiant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }*/

    public String toString() {
        return "Etudiant : " + this.nom + ", " + this.prenom + " en " + this.diplome + 
                "\n_Assurance : " + this.assurance + ", no " + this.contrat;
        
    }
    
}
    

