package Food4One.app.View.MainScreen.MainScreenFragments.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipeRepository;
import Food4One.app.R;
import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

public class RotateActivity extends AppCompatActivity {
    private LuckyWheelView luckyWheelView;
    private LottieAnimationView arrowAnimated;
    private TextView titleSurprise;
    private Button startSurprise, restartSurprise;
    private Spinner optionsRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel);
        getSupportActionBar().hide();

        initViewSources();
        listenerToWheel();
        buttonsListeners();
        recipeOptionsSurprise();

    }

    private void listenerToWheel() {// listener after finish lucky wheel
        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                // do something with index
                Toast.makeText(RotateActivity.this, Integer.toString(index), Toast.LENGTH_SHORT).show();
                lottieAnimationArrow();
                showTitleSurpriseAnim();
            }
        });
    }

    private void recipeOptionsSurprise() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.options_Surprise, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsRecipes.setAdapter(adapter);

        optionsRecipes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                String option="";
                // Aquí se realizan las acciones basadas en la opción seleccionada
                if(selectedOption.equals("Salado"))
                    option= "Pasta Arroz";
                else if (selectedOption.equals("Dulce"))
                    option = "Pastel Bocatas";
                else
                    option = selectedOption; //Bebidas

                loadOptionsRecipesSurprise(option); //Cargamos las recetas dadas
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Este método se llama cuando no se selecciona ninguna opción
                //Por defecto cargaremos recetas de pasta y de arroz, que son los más comunes
                loadOptionsRecipesSurprise("Pasta Arroz");
            }
        });
    }

    private void lottieAnimationArrow() {
        arrowAnimated.setMinAndMaxProgress(0.0f, 1.0f);
        arrowAnimated.loop(true);
        arrowAnimated.playAnimation();
    }
    private void stopAnimationArrow(){
        arrowAnimated.setMinAndMaxProgress(0.0f, 0.0f);
        arrowAnimated.playAnimation();
        arrowAnimated.cancelAnimation();
    }
    private void initViewSources() {
        luckyWheelView = (LuckyWheelView) findViewById(R.id.luckyWheel);
        arrowAnimated = findViewById(R.id.lottieFlecha);
        titleSurprise = findViewById(R.id.tituloSurprise);
        startSurprise = findViewById(R.id.buttonSurprise);
        restartSurprise =findViewById(R.id.buttonRepeatSurprise);
        optionsRecipes = this.findViewById(R.id.spinner_options);
    }

    private void luckyWheelStructure(HashMap<Integer, Recipe> recetas) {

        ArrayList<LuckyItem> data = new ArrayList<>();

        for (int i=0; i<6;i++) {
            LuckyItem luckyItem = new LuckyItem();
            luckyItem.topText = recetas.get(i).getNombre();
            luckyItem.color = 0xffFFF3E0;
            data.add(luckyItem);
        }

        luckyWheelView.setData(data);
        luckyWheelView.setRound(5);


    }

    private void showTitleSurpriseAnim() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animation.setDuration(2000);
        titleSurprise.setText("Receta Encontrada");
        titleSurprise.startAnimation(animation);
    }
    private void hideTitleSurpriseAnim(){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        //Esperamos a que la Animación de desaparecer el título se realice para poder poner el
        //texto vacío y así no verlo ("Receta Encontrada")
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                 titleSurprise.setText("");
            }
        }, 700);

        animation.setDuration(700);
        titleSurprise.startAnimation(animation);

    }


    private void buttonsListeners() {
        startSurprise.setOnClickListener(v->{
            stopAnimationArrow();
            hideTitleSurpriseAnim();
            //Start Wheel
            luckyWheelView.startLuckyWheelWithRandomTarget();
        });

        restartSurprise.setOnClickListener(v -> {
            stopAnimationArrow();
            hideTitleSurpriseAnim();
        });
    }


    private void loadOptionsRecipesSurprise(String type){
        //Obtengo todas las recetas que hay de la App guardadas en el HashMap del View Model
        HashMap<String , ArrayList<Recipe>> recetasApp = HomeViewModel.getInstance().getRecetasApp();
        //Separo los dos tipos que se me piden
        String[] anyType = type.split(" ");

        //Creo un hashMap que contendrá las recetas con sus respectivas posiciones en la ruleta
        HashMap<Integer, Recipe> positions= new HashMap<Integer, Recipe>();
        Random r = new Random();
        int pos=0, posHashmap=0;
        ArrayList<Recipe> recetas;

        //Para los dos tipos cargo las recetas (Si hace falta cargalos)
        for(String tipo: anyType){
            recetas = recetasApp.get(tipo);
            //Si no se ha cargado ya ese tipo de recetas, entonces hacemos un acceso a la Base de datos
            if(recetas.size()==0)
                RecipeRepository.getInstance().loadRecipesApp(recetas,tipo);

            //Ahora seleccionamos las recetas random de ese grupo
            for(int i=0; i<3; i++) {
                recetas = recetasApp.get(tipo);
                pos = r.nextInt(recetas.size() - 1);
                //Añadimos en la
                positions.put(posHashmap, recetas.get(pos));
                posHashmap++;
            }

        }

        luckyWheelStructure(positions);
    }


}
