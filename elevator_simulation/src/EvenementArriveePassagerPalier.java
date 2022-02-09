public class EvenementArriveePassagerPalier extends Evenement {
	/* APP: Arrivee Passager Palier
       L'instant precis ou un nouveau passager arrive sur un palier donne, dans le but
       de monter dans la cabine.
	 */

	private Etage etage;

	public EvenementArriveePassagerPalier(long d, Etage edd) {
		super(d);
		etage = edd;
	}

	public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
		buffer.append("APP ");
		buffer.append(etage.numero());
	}

	public void traiter(Immeuble immeuble, Echeancier echeancier) {
		assert etage != null;
		assert immeuble.etage(etage.numero()) == etage;
		Passager passager = new Passager(date, etage, immeuble);
		Cabine cabine = immeuble.cabine;

		if (cabine.porteOuverte && cabine.etage == etage) {
			if (cabine.intention() == '-') {

				cabine.changerIntention(passager.sens());
				echeancier.ajouter(new EvenementFermeturePorteCabine(date + tempsPourOuvrirOuFermerLesPortes)); 
				char fmp = cabine.faireMonterPassager(passager);
				// Faudrait aussi ajouter le premier PCP...
				if (fmp == 'O') {
					assert true;
				} else {
					assert false : "else impossible";
				};	
			} else  if (cabine.intention() == '^'){
				etageSuivant = immeuble.etage(cabine.etage.numero()+1);
				assert etageSuivant.numero() == cabine.etage.numero() + 1;
			};
		} else {
			notYetImplemented();
		};

		assert cabine.intention() != '-' : "intention impossible";
	}

}