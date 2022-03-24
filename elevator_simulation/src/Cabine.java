import javax.lang.model.element.Element;
import java.util.Date;

public class Cabine extends Global {
	/* Dans cette classe, vous pouvez ajouter/enlever/modifier/corriger les méthodes, mais vous ne
       pouvez pas ajouter des attributs (variables d'instance).
	 */

	public Etage etage; // actuel, là ou se trouve la Cabine, jamais null.

	public boolean porteOuverte;

	private char intention; // '-' ou 'v' ou '^'

	private Passager[] tableauPassager;
	/* Ceux qui sont actuellement dans la Cabine. On ne décale jamais les élements.
       Comme toute les collections, il ne faut pas l'exporter.
       Quand on cherche une place libre, on fait le parcours de la gauche vers la droite.
	 */

	public Cabine(Etage e) {
		assert e != null;
		etage = e;
		tableauPassager = new Passager[nombreDePlacesDansLaCabine];
		porteOuverte = true;
		intention = '-';
	}

	public void afficheDans(StringBuilder buffer) {
		buffer.append("Contenu de la cabine: ");
		for (Passager p: tableauPassager) {
			if (p == null) {
				buffer.append("null");		
			} else {
				p.afficheDans(buffer);
			}
			buffer.append(' ');
		}
		assert (intention == '-') || (intention == 'v') || (intention == '^');
		buffer.append("\nIntention de la cabine: " + intention);
	}

	/* Pour savoir si le passager p est bien dans la Cabine.
       Attention, c'est assez lent et c'est plutôt une méthode destinée à être 
       utilisée dans les asserts.
	 */
	public boolean transporte(Passager p) {
		assert p != null;
		for (int i = tableauPassager.length - 1 ; i >= 0  ; i --) {
			if (tableauPassager[i] == p) {
				return true;
			}
		}
		return false;
	}

	public char intention() {
		assert (intention == '-') || (intention == 'v') || (intention == '^');
		return intention;
	}

	public void changerIntention(char s){
		assert (s == '-') || (s == 'v') || (s == '^');
		intention = s;
	}

	public char faireMonterPassager(Passager p) {
		assert p != null;
		assert ! transporte(p);
		if (modeParfait) {
			if (intention != p.sens()) {
				return 'I'; // Intention
			}
		}
		for (int i=0 ; i<tableauPassager.length ; i++) {
			if(tableauPassager[i]==null){
				tableauPassager[i]=p;
				return 'O'; // comme Ok
			}
		}
		return 'P'; // Plein
	}

	public int faireDescendrePassagers(Immeuble immeuble,long d){
		int c=0;
		int i=tableauPassager.length-1;
		while(i>=0){
			Passager p = tableauPassager[i];
			if(p != null){
				assert transporte(p);
				if(p.etageDestination() == etage){
					immeuble.ajouterCumul(d-p.dateDepart());
					immeuble.nombreTotalDesPassagersSortis++;
					tableauPassager[i]=null; 
					c++;
				}
			}
			i--;
		}
		return c;
	}

	public boolean passagersVeulentDescendre(){
		int i=tableauPassager.length-1;
		while(i>=0){
			Passager p = tableauPassager[i];
			if(p!=null){
				assert transporte(p);
				if(p.etageDestination() == etage){
					return true;
				}
			}
			i--;
		}
		return false;
	}

	public void changerEtage (Echeancier echeancier, long date){
		if(this.intention() == '^'){
			echeancier.ajouter(new EvenementPassageCabinePalier(date + tempsPourBougerLaCabineDUnEtage, this.etage.getImmeuble().etage(this.etage.numero()+1)));

		}else if(this.intention() == 'v'){
			echeancier.ajouter(new EvenementPassageCabinePalier(date + tempsPourBougerLaCabineDUnEtage, this.etage.getImmeuble().etage(this.etage.numero()-1)));

		}
	}

	public Boolean doitSarreter (Echeancier echeancier, long date, Immeuble immeuble){
		if(this.passagersVeulentDescendre()){
			return true;
		} else if(immeuble.etageADesPassage(etage.numero())){
			return true;
		}
		return false;
	}

	public void ouvrirPorte() {
		porteOuverte =true;
	}
	
	public void fermerPorte() {
		porteOuverte =false;
	}

	public boolean isEmpty(){
		if (this.tableauPassager[0]==null)
			return true;
		else return false;
	}
	public void recalculerIntention(Echeancier echeancier,Immeuble immeuble,long date){
		assert (this.intention!='-');
		if (intention=='^'&&immeuble.etageLePlusHaut().numero()>etage.numero()) {
			if (etage.aDesPassagersQuiMontent()) {
				return;
			} else if (immeuble.passagerAuDessus(etage)) {
				return;
			} else if (etage.aDesPassagersQuiDescendent() || immeuble.passagerEnDessous(etage)) {
				changerIntention('v');
			}else changerIntention('-');
		}else if (intention=='v'&&immeuble.etageLePlusBas().numero()<etage.numero()){
			if (etage.aDesPassagersQuiDescendent()) return;
		 else if (immeuble.passagerEnDessous(etage)) {
			return;
		} else if (etage.aDesPassagersQuiMontent() || immeuble.passagerAuDessus(etage)) {
		changerIntention('^');
		}else changerIntention('-');
		}
		else if (intention=='v' && immeuble.etageLePlusBas().numero()==etage.numero()){
			if (etage.aDesPassagersQuiMontent()){
				changerIntention('^');
			}else if (immeuble.passagerAuDessus(etage)){
				changerIntention('^');
			}else changerIntention('-');
		}
		else if (intention=='^' && immeuble.etageLePlusHaut().numero()==etage.numero()){
		if (etage.aDesPassagersQuiDescendent()){
			changerIntention('v');
		}else if (immeuble.passagerEnDessous(etage)){
			changerIntention('v');
		}else changerIntention('-');
	}
	}
}

