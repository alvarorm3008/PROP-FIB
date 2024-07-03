package domini;

/**
* Classe que s'encarrega de la resolució d'un Kenken. Conté els mètodes per tal de dur a terme aquesta resolució mitjançat backtracking i altres mètodes per millor la seva eficiència.
*/
public class ResolverKenKen {
    /**
     * Objecte Kenken que es vol resoldre.
     */
    private Kenken KK;

    /**
     * Constructora de la classe.
     * @param kk Objecte Kenken que es vol resoldre.
     */
    public ResolverKenKen(Kenken kk) {
        KK = kk;
    }

    /**
     * Mètode que comprova el resultat de les regions amb caselles úniques si coincideix amb el valor introduit a la casella, i si no tenen valor, les marca com a bloquejades i
     * assigna el resultat de la regió al valor de la casella.
     */
    public boolean casellesuniques() {
        for(int i = 0; i < KK.getRegionsKK().size(); ++i) {
            if (KK.getRegioI(i).getnum_caselles() == 1) {
                //Si la regió només té una casella i aquesta casella no té cap valor introduit llavors s'afegeix el valor del resultat.
                if (!KK.getRegioI(i).getCasellaDeRegio(0).estaBloquejada()) {
                    int res = KK.getRegioI(i).getResultat();
                    if (res > 0 && res <= KK.getTam()) {
                        KK.getRegioI(i).getCasellaDeRegio(0).setNumero(res);
                        KK.getRegioI(i).getCasellaDeRegio(0).bloquejar();
                    }
                }
                else {
                    //Si la casella ja té un valor introduít es comprova que aquest valor sigui correcte.
                    int valorcasella = KK.getRegioI(i).getCasellaDeRegio(0).getNumero();
                    int res = KK.getRegioI(i).getResultat();
                    if (valorcasella != res) return false;
                }
            }
        }
        return true;
    }

    /**
     * Algorisme que resol un Kenken mitjançant backtracking.
     * @param x posició inicial de la fila
     * @param y posició incial de la columna
     * @return Retorna true si el Kenken es pot resoldre, false altrament.
     */
    public boolean resoldre(int x, int y) {
        //Comprova si ha mirat totes les caselles del kenken.
        if (x == KK.getTam()+1) {
            return true;
        }
        //Salta a la seguent fila del kenken.
        if (y == KK.getTam()+1) {
            return resoldre(x+1, 1);
        }
        //Si la casella està bloquejada, no es comprova la casella, salta a la seguent.
        if (KK.getCasellaI(x, y).estaBloquejada()) return resoldre(x, y+1);
        //Per cada valor possible comprova si es pot col·locar comprovant si no hi ha cap altre valor a la mateixa fila i columna.
        for (int valor = 1; valor <= KK.getTam(); valor++) {
            if (KK.comprovaFila(x, y, valor) && KK.comprovaColumna(x, y, valor)) {
                KK.setNumeroCasella(x, y, valor);
                //Si la regió està completa, comprova si el resultat de la regió és correcte amb els valors col·locats.
                if (KK.getRegioI(KK.idRegio(x, y)).regCompleta()) {
                    if (!KK.getRegioI(KK.idRegio(x, y)).comprovaResultat()) KK.borraCasella(x, y);
                    else if (KK.calcularCasellesRestants() == 0) return true;
                    else {
                        if (resoldre(x, y + 1)) return true;
                        KK.borraCasella(x, y);
                    }
                }
                //Si la regió no està completa, comprovo que amb els valors col·locats no em paso del resultat.
                else if (KK.getRegioI(KK.idRegio(x, y)).checkRegio()) {
                    boolean trobat = resoldre(x, y+1);
                    if(trobat) return true;
                    KK.borraCasella(x, y);
                }
                else KK.borraCasella(x, y);
            }
        }
        return false;
    }
}
