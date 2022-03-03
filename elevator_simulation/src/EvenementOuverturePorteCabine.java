public class EvenementOuverturePorteCabine extends Evenement {
    /* OPC: Ouverture Porte Cabine
       L'instant précis ou la porte termine de s'ouvrir.
    */
    
    public EvenementOuverturePorteCabine(long d) {
	super(d);
    }
    
    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
	buffer.append("OPC");
    }
    
    public void traiter(Immeuble immeuble, Echeancier echeancier) {
		Cabine cabine = immeuble.cabine;
		Etage etage = cabine.etage;

		assert !cabine.porteOuverte;
		cabine.changerIntention('-');
		cabine.faireDescendrePassagers(immeuble, date);
		cabine.ouvrirPorte();
		assert cabine.porteOuverte;
		if (etage.aDesPassagers()) {
			for (int i = 0; i < etage.getPassagers().size(); i++) {
				cabine.faireMonterPassager(etage.getPassagers().get(i));
				etage.getPassagers().remove(i);
			}
			echeancier.ajouter(new EvenementFermeturePorteCabine(date + tempsPourOuvrirOuFermerLesPortes+tempsPourEntrerOuSortirDeLaCabine));
		}
	}
}
