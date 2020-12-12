package fr.lineac.squaro2;



import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    fr.lineac.squaro2.CadreSquaro mCadre;
    Button mBoutonRejouer =null;
    Button mBoutonVerifier =null;
    TextView mTvInfo =null;
    CheckBox mCheckBoxAideCouleur;
    int nbClick =0;
    String chaineInfo;
    private static final String TAG = "MainActivity";
    boolean aideCouleurActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mCadre = findViewById(R.id.activity_main_grille_canvas);
        mBoutonRejouer =findViewById(R.id.activity_main_rejouer_button);
        mBoutonVerifier =findViewById(R.id.activity_main_verifier_button);
        mCheckBoxAideCouleur =findViewById(R.id.activity_main_couleur_checkBox);
        mCheckBoxAideCouleur.setChecked(aideCouleurActive);
        mCadre.aideCouleur=aideCouleurActive;
        Log.i(TAG,"hello");

        // Listeners

        mCadre.setOnTouchListener(new View.OnTouchListener(){
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent me) {
                int l;
                int c;
                mTvInfo =findViewById(R.id.ativity_main_info_textView);
                c=numCol(me.getX());
                l=numLig(me.getY());
                //chaineInfo+="  " + getString(R.string.coordonneesClick,c,l);

                if ((c>=0)&&(c<= mCadre.nbCol)){
                    if((l>=0)&&(l<= mCadre.nbLig)){
                        nbClick++;
                        chaineInfo=getString(R.string.numClick, nbClick);
                        mTvInfo.setText(chaineInfo);
                        mCadre.tabPastilles[l][c]=1- mCadre.tabPastilles[l][c];
                        mCadre.invalidate();
                    }
                }
                Log.i(TAG,"click "+nbClick);
                return false;
            }
        });

        mCheckBoxAideCouleur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                aideCouleurActive=isChecked;
                mCadre.aideCouleur=aideCouleurActive;
                mCadre.invalidate();
            }
        });

        mBoutonRejouer.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 TextView tv;
                                                 mCadre.init();
                                                 mCadre.invalidate();
                                                 tv=findViewById(R.id.ativity_main_info_textView);
                                                 tv.setText(R.string.info);
                                                 nbClick =0;
                                             }
                                         }
        );

        mBoutonVerifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean gagne=true;
                TextView tv=findViewById(R.id.ativity_main_info_textView);
                for(int i = 0; i< mCadre.nbLig; i++){
                    for(int j = 0; j< mCadre.nbCol; j++){
                        int nbPoints=0;
                        for(int k=0;k<2;k++){
                            for(int l=0;l<2;l++){
                                nbPoints+= mCadre.tabPastilles[i+k][j+l];
                            }
                        }
                        if (nbPoints!= mCadre.tabValeurs[i][j]) gagne=false;
                    }
                }
                if (gagne){

                    tv.setText(getString(R.string.bravo,nbClick));
                }
                else{
                    tv.setText(getString(R.string.essaieEncore));
                }
            }
        });
    }


    public int numCol(float xClick){
        int col;
        float deltaX= mCadre.deltaX;
        float xMin= mCadre.xMin;
        float offset=xMin-deltaX/2;
        col= (int)Math.floor((xClick-offset)/deltaX);
        return col;
    }
    public int numLig(float yClick){
        int lig;
        float deltaY= mCadre.deltaY;
        float yMin= mCadre.yMin;
        float offset=yMin-deltaY/2;
        lig= (int)Math.floor((yClick-offset)/deltaY);
        return lig;
    }

}
