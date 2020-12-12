package fr.lineac.squaro2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;


public class CadreSquaro extends View {
    private Paint pCercleJaune = new Paint();
    private Paint pCercleNoir =new Paint();
    private Paint pChiffres=new Paint();
    private Paint pLigne=new Paint();
    public float xMin,yMin,deltaX,deltaY,rayonDisque;
    public int deltaXDp, deltaYDp, rayonDisqueDp;
    public int nbCol,nbLig;
    public int curLig,curCol;
    public float largeurTrait;
    public int[][] tabValeurs;
    public int[][] tabPastilles;
    public int[][] tabPastillesSolution;
    public boolean aideCouleur = false;

    // valeurs en dp
    public final int DEFAULT_R_DISQUES_DP=6;
    public final int DEFAULT_LARGEUR_TRAIT=3;
    public final int DEFAULT_DELTA_X_DP=60;
    public final int DEFAULT_DELTA_Y_DP =60;
    public final int DEFAULT_NB_COL=5;
    public final int DEFAULT_NB_LIG=5;
    private static final String TAG = "CadreSquaro";

    public CadreSquaro(Context context, AttributeSet attrs) {

        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas c) {

       super.onDraw(c);

       /* Disques */
        afficheDisques(c);

       /* Lignes H */
        afficheLignesH(c);
       /* Lignes V */
       afficheLignesV(c);

         /* Chiffres */
        afficheChiffres(c);
    }

    public void afficheDisques(Canvas c){
        for (curLig=0;curLig<=nbLig;curLig++){
            for(curCol=0;curCol<=nbCol;curCol++){
                c.drawCircle(xMin+curCol*deltaX,yMin+curLig*deltaY,rayonDisque,(tabPastilles[curLig][curCol]==1? pCercleJaune : pCercleNoir));
            }
        }
    }

    public void afficheLignesV(Canvas c){
        for(curCol=0;curCol<=nbCol;curCol++){
            for(curLig=0;curLig<nbLig;curLig++){
                c.drawLine( xMin+curCol*deltaX,yMin+curLig*deltaY+rayonDisque,xMin+curCol*deltaX,yMin+(curLig+1)*deltaY-rayonDisque,pLigne);
            }
        }
    }

    public void afficheLignesH(Canvas c){
        for(curLig=0;curLig<=nbLig;curLig++){
            for(curCol=0;curCol<nbCol;curCol++){
                c.drawLine(xMin+rayonDisque+curCol*deltaX,yMin+curLig*deltaY,xMin-rayonDisque+(curCol+1)*deltaX,yMin+curLig*deltaY,pLigne);
            }
        }
    }

    public void afficheChiffres(Canvas c){
        for(curLig=0;curLig<nbLig;curLig++){
            for(curCol=0;curCol<nbCol;curCol++){
                if(aideCouleur) {
                    pChiffres.setColor((verifieCase(curLig, curCol) ? getResources().getColor(R.color.colorBon ) : Color.BLACK));
                    pChiffres.setFakeBoldText(true);
                }
                else {
                    pChiffres.setColor(Color.BLACK);
                    pChiffres.setFakeBoldText(false);
                }
                 c.drawText(String.valueOf(tabValeurs[curLig][curCol]),xMin+(curCol+(float)0.5)*deltaX,yMin+(curLig+(float)0.5)*deltaY+pChiffres.getTextSize()/2,pChiffres);
            }
        }
    }

    public float convDpToPx(int dp) {
        DisplayMetrics dm = getResources().getDisplayMetrics() ;
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
    }


    public boolean verifieCase(int i, int j){
        int nbPoints=0;
        boolean ok=true;
        for(int k=0;k<2;k++){
            for(int l=0;l<2;l++){
                nbPoints+=tabPastilles[i+k][j+l];
            }
        }
        if (nbPoints!=tabValeurs[i][j]) ok=false;
        return ok;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public Point tailleGrilleDp(){
        Point tailleDp =new Point();
        tailleDp.x=2*rayonDisqueDp+nbCol*deltaXDp;
        tailleDp.y=2*rayonDisqueDp+nbLig*deltaYDp;
        return tailleDp;
    }

    public Point tailleGrillePx(){
        Point taillePx=new Point();
        Point tailleDp=tailleGrilleDp();
        taillePx.x= (int) convDpToPx(tailleDp.x);
        taillePx.y=(int) convDpToPx(tailleDp.y);
        return  taillePx;
    }

    public Point coinSupGauche(){
        Point pointSupGauche=new Point();
        Point tailleEcranPx= getTailleEcranPixel();
        Point tailleGrillePx=tailleGrillePx();
        pointSupGauche.x=(tailleEcranPx.x-tailleGrillePx.x)/2;
        pointSupGauche.y=(tailleEcranPx.y-tailleGrillePx.y)/2;
        Log.i(TAG, "coinSupGauche: taille ecran : "+tailleEcranPx.x+" - "+tailleEcranPx.y);
        Log.i(TAG, "coinSupGauche: taille grille : "+tailleGrillePx.x+" - "+tailleGrillePx.y);
        Log.i(TAG, "coinSupGauche: point sup gauche : "+pointSupGauche.x+" - "+pointSupGauche.y);
        return pointSupGauche;
    }

    public Point getTailleEcranPixel() {
        /*Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return(size);*/
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Point size = new Point();
        size.y = displayMetrics.heightPixels;
        size.x = displayMetrics.widthPixels;

        return size;
    }

    public void init(){
        nbCol=DEFAULT_NB_COL;
        nbLig=DEFAULT_NB_LIG;
        deltaXDp=DEFAULT_DELTA_X_DP;
        deltaYDp= DEFAULT_DELTA_Y_DP;
        rayonDisqueDp=DEFAULT_R_DISQUES_DP;


        //xMin= convDpToPx(40);
        yMin=convDpToPx(10);
        deltaX=convDpToPx(deltaXDp);
        deltaY=convDpToPx(deltaYDp);
        rayonDisque=convDpToPx(rayonDisqueDp);
        largeurTrait=convDpToPx(DEFAULT_LARGEUR_TRAIT);
        Point pointSupGauche=coinSupGauche();
        xMin=pointSupGauche.x;
        //yMin=pointSupGauche.y;
        tabValeurs=new int[nbLig][nbCol];
        tabPastilles=new int[nbLig+1][nbCol+1];
        tabPastillesSolution=new int[nbLig+1][nbCol+1];

        for (int i=0;i<=nbLig;i++){
            for(int j=0;j<=nbCol;j++){
                tabPastillesSolution[i][j]= Math.round((float) Math.random());
                tabPastilles[i][j]=0;
             }
        }
        for (int i=0;i<nbLig;i++){
            for(int j=0;j<nbCol;j++){
                tabValeurs[i][j]=0;
                for(int k=0;k<2;k++){
                    for(int l=0;l<2;l++){
                        tabValeurs[i][j]+=tabPastillesSolution[i+k][j+l];
                    }
                }
            }
        }

        // Configuration d'un crayon gris
        // Configuration d'un crayon pour cercle noir et blanc
        pLigne.setColor(Color.GRAY);
        pLigne.setStyle(Paint.Style.FILL);
        pLigne.setStrokeWidth(largeurTrait);


        pCercleJaune.setColor(Color.YELLOW);
        pCercleJaune.setStyle(Paint.Style.FILL_AND_STROKE);
        pCercleJaune.setStrokeWidth(1);
        pCercleJaune.setAntiAlias(true);

        pCercleNoir.setColor(Color.BLACK);
        pCercleNoir.setStyle(Paint.Style.FILL_AND_STROKE);
        pCercleNoir.setStrokeWidth(1);
        pCercleNoir.setAntiAlias(true);

        pChiffres.setColor(Color.DKGRAY);
        pChiffres.setTextAlign(Paint.Align.CENTER);
        pChiffres.setTextSize(convDpToPx(20));



    }
}
