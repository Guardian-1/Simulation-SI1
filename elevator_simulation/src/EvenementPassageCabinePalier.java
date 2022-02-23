public class EvenementPassageCabinePalier extends Evenement {
    /* PCP: Passage Cabine Palier
       L'instant precis où la cabine passe juste en face d'un etage precis.
       Vous pouvez modifier cette classe comme vous voulez (ajouter/modifier des methodes etc.).
    */
    
    private Etage etage;
    
    public EvenementPassageCabinePalier(long d, Etage e) {
	super(d);
	etage = e;
    }
    
    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
	buffer.append("PCP ");
	buffer.append(etage.numero());
    }
    
    public void traiter(Immeuble immeuble, Echeancier echeancier) {
	Cabine cabine = immeuble.cabine;
	assert ! cabine.porteOuverte;
	assert etage.numero() != cabine.etage.numero();
		cabine.etage = etage;

		int etageNum = cabine.etage.numero();
		if (etage == cabine.etage){
			echeancier.ajouter(new EvenementOuverturePorteCabine(date + tempsPourOuvrirOuFermerLesPortes));
		}
		if (cabine.intention() =='-'){
			echeancier.ajouter(new EvenementOuverturePorteCabine(date + tempsPourOuvrirOuFermerLesPortes));
		}
		if(cabine.intention() == '^'){
			echeancier.ajouter(new EvenementPassageCabinePalier(date + tempsPourBougerLaCabineDUnEtage, immeuble.etage(etageNum+1)));
		}else if(cabine.intention() == 'v'){
			echeancier.ajouter(new EvenementPassageCabinePalier(date + tempsPourBougerLaCabineDUnEtage, immeuble.etage(etageNum-1)));
		}
	//cabine.changerEtage(echeancier,date);

    }
}
