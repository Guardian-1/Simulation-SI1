public class EvenementArriveePassagerPalier extends Evenement {
    /* APP: Arrivée Passager Palier
       L'instant précis ou un nouveau passager arrive sur un palier donné, dans le but
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
	Passager p = new Passager(date, etage, immeuble);
	Cabine c = immeuble.cabine;


	if (c.porteOuverte && c.etage == etage) {
	    if (c.intention() == '-') {
		c.changerIntention(p.sens());
		echeancier.ajouter(new EvenementFermeturePorteCabine(date + tempsPourOuvrirOuFermerLesPortes)); 
		char fmp = c.faireMonterPassager(p);
		if (fmp == 'O') {
		    assert true;
		} else {
		    assert false : "else impossible";
		};	
	    //} else {
			//echeancier.ajouter(new EvenementPassageCabinePalier(date + tempsPourBougerLaCabineDUnEtage,p.etageDestination()));

		};
	} else {
		if (c.etage != etage){
		 echeancier.ajouter(new EvenementPassageCabinePalier(date + tempsPourBougerLaCabineDUnEtage,p.etageDestination()));
		}
		if (!c.porteOuverte && c.etage == etage)
			echeancier.ajouter(new EvenementOuverturePorteCabine(date+tempsPourOuvrirOuFermerLesPortes));
		c.faireMonterPassager(p);
		c.changerIntention(p.sens());
	}
	date+=etage.arriveeSuivante();
	echeancier.ajouter(this);
	assert c.intention() != '-' : "intention impossible";
    }

}
