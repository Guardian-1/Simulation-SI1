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
		int c = 0;
		assert !cabine.porteOuverte;
		//cabine.changerIntention('-');
		c = cabine.faireDescendrePassagers(immeuble, date);
		cabine.ouvrirPorte();
		cabine.recalculerIntention(echeancier,immeuble,date);
		if((etage.aDesPassagersQuiDescendent() && cabine.intention() == 'v') || (etage.aDesPassagersQuiMontent() && cabine.intention() == '^')) {
			c = c + etage.faireEntrerPassager(cabine,echeancier);
		}
		
		if (!immeuble.immeubleVide() || !cabine.isEmpty() && cabine.porteOuverte) {
			echeancier.ajouter(new EvenementFermeturePorteCabine(date + tempsPourOuvrirOuFermerLesPortes + (c * tempsPourEntrerOuSortirDeLaCabine)));
		};
		assert cabine.porteOuverte;


	}
}
