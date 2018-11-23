package servicejuridique.sources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marieroca
 */
public class Diplome implements Serializable{
    //private static final long serialVersionUID = 1L;

    private Long id;
    
    public enum Niveau{
        L1, L2, L3, M1, M2
    };
    private Niveau niveau;
    private String intitule;
    private List<Etudiant> etudiants = new ArrayList<>();

    public Diplome(Long id, Niveau niveau, String intitule) {
        this.id = id;
        this.niveau = niveau;
        this.intitule = intitule;
    }

    public Long getId() {
        return id;
    }

    /*public void setId(Long id) {
        this.id = id;
    }*/

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    /*public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Diplome)) {
            return false;
        }
        Diplome other = (Diplome) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }*/

    public String toString() {
        //return "entities.Diplome[ id=" + id + " ]";
        return "Diplome : " + this.niveau + " " + this.intitule;
    }
}
