package Food4One.app.View.MainScreen.MainScreenFragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipeRepository;
import Food4One.app.R;
import Food4One.app.View.MainScreen.MainScreenFragments.Coleccion.RecyclerViewAdapter;
import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

public class RotateActivity extends AppCompatActivity {
    private Integer posHashmap=0, NUMBER_ELEMENTS_SURPRISE = 6;
    private TextView titleSurprise, recipeSurpriseName;
    HashMap<String , ArrayList<Recipe>> recetasApp;
    private Button startSurprise, restartSurprise;
    //Creo un hashMap que contendrá las recetas con sus respectivas posiciones en la ruleta
    private HashMap<Integer, Recipe> positions;
    private LottieAnimationView arrowAnimated;
    private RotateViewModel rotateViewModel;
    private LuckyWheelView luckyWheelView;
    private ImageView recipeSurpriseImage;
    //Array de Items de la Rueda, contendrá los nombres de las recetas.
    private ArrayList<LuckyItem> data;
    private Spinner optionsRecipes;
    private Recipe recipeSurprise;
    private CardView cardView;
    private String option;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel);
        getSupportActionBar().hide();

        rotateViewModel = RotateViewModel.getInstance();

        initViewSources();
        listenerToWheel();
        buttonsListeners();
        recipeOptionsSurprise();
        recipesCargadasObserver();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        positions = new HashMap<>();
        data = new ArrayList<>();
        posHashmap=0;
        luckyWheelView.restartWheel();
    }

    private void initViewSources() {
        luckyWheelView = (LuckyWheelView) findViewById(R.id.luckyWheel);
        arrowAnimated = findViewById(R.id.lottieFlecha);
        titleSurprise = findViewById(R.id.tituloSurprise);
        startSurprise = findViewById(R.id.buttonSurprise);
        restartSurprise =findViewById(R.id.buttonRepeatSurprise);
        optionsRecipes = this.findViewById(R.id.spinner_options);
        recipeSurpriseName = findViewById(R.id.recipeSurpriseName);
        recipeSurpriseImage = findViewById(R.id.recipeSurpriseImage);
        cardView = findViewById(R.id.recipeSurpriseCard);
        cardView.setVisibility(View.GONE);
        data = new ArrayList<>();
        positions = new HashMap<>();
        luckyWheelView.setData(data);
    }

    private void recipesCargadasObserver() {
        //Cuando se complete el cargado del tipo que se ha elegido...
        final Observer<String> completed = new Observer<String>() {
            @Override
            public void onChanged(String tipo) {

                if (!tipo.equals("")) {
                    int pos = 0;
                    ArrayList<Recipe> recetas;
                    Random r = new Random();

                    recetas = recetasApp.get(tipo);

                    //Para no repetir una receta con el random...
                    ArrayList<Integer> numbers = new ArrayList<>();
                    // Agrega los números que podemos utilizar para el random
                    for (int i = 0; i < recetas.size(); i++) {
                        numbers.add(i);
                    }
                    int totals=0;
                    if(tipo.equals("Bebidas")) totals=NUMBER_ELEMENTS_SURPRISE;
                    else totals = (NUMBER_ELEMENTS_SURPRISE/2);

                    //Ahora seleccionamos las recetas random de ese grupo
                    for (int i = 0; i < totals; i++) {
                        pos = r.nextInt(numbers.size());
                        //Añadimos en la
                        positions.put(posHashmap, recetas.get(numbers.get(pos)));
                        numbers.remove(pos);
                        posHashmap++;
                    }

                    if(posHashmap == NUMBER_ELEMENTS_SURPRISE)
                        luckyWheelStructure(positions);

                    rotateViewModel.setCompleted("");
                }
            }
        };
        rotateViewModel.getCompleted().observe(this, completed);
    }

    private void listenerToWheel() {// listener after finish lucky wheel
        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                //Aquí obtenemos el índice del resultado en la ruleta
                lottieAnimationArrow();
                showTitleSurpriseAnim();

                //Ya tenemos un HashMap con la receta llaveada con el índice, se la pedimos...
                recipeSurprise = positions.get(index);
                recipeSurpriseName.setText(recipeSurprise.getNombre());
                Picasso.get().load(recipeSurprise.getPictureURL())
                        .resize(800, 660)
                        .centerCrop().into(recipeSurpriseImage);
                cardView.setVisibility(View.VISIBLE);
                cardView.setAnimation( AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in) );

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
                // Aquí se realizan las acciones basadas en la opción seleccionada
                if(selectedOption.equals("Arroz y Pasta"))
                    option= "Pasta Arroz";
                else if (selectedOption.equals("Dulce y Bocatas"))
                    option = "Pastel Bocatas";
                else
                    option = selectedOption; //Bebidas

                loadOptionsRecipesSurprise(option); //Cargamos las recetas dadas
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Este método se llama cuando no se selecciona ninguna opción
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

    private void luckyWheelStructure(HashMap<Integer, Recipe> recetas) {


        for (int i=0; i< recetas.size(); i++) {
            LuckyItem luckyItem = new LuckyItem();
            luckyItem.topText = recetas.get(i).getNombre();
            luckyItem.color = 0xffFFF3E0;
            data.add(luckyItem);
        }
            luckyWheelView.setData(data);
            luckyWheelView.setRound(5);
    }

    private void showTitleSurpriseAnim() {
        if(titleSurprise.getText().equals("")) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            animation.setDuration(2000);
            titleSurprise.setText("Receta Encontrada");
            titleSurprise.startAnimation(animation);
        }
    }
    private void hideTitleSurpriseAnim(){
        if(! titleSurprise.getText().equals("")) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
            //Esperamos a que la Animación de desaparecer el título se realice para poder poner el
            //texto vacío y así no verlo ("Receta Encontrada")
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    cardView.setVisibility(View.GONE);
                    titleSurprise.setText("");
                }
            }, 700);

            animation.setDuration(700);
            titleSurprise.startAnimation(animation);
            cardView.startAnimation(animation);
        }

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
            positions = new HashMap<>();
            loadOptionsRecipesSurprise(option);

        });

        cardView.setOnClickListener(v->{
            HomeViewModel.getInstance().loadRecipeToMake(recipeSurprise);
            startActivity(new Intent(this, DoRecipeActivity.class));
        });
    }


    private void loadOptionsRecipesSurprise(String type){
        //Volveremos a cargar las recetas por lo tanto, toca reiniciar la dirección del HashMap
        posHashmap=0;
        luckyWheelView.restartWheel();

        //Obtengo todas las recetas que hay de la App guardadas en el HashMap del View Model
        recetasApp = HomeViewModel.getInstance().getRecetasApp();
        //Separo los dos tipos que se me piden
        String[] anyType = type.split(" ");

        ArrayList<Recipe> recetas;
        //Para los dos tipos cargo las recetas (Si hace falta cargalos)
        if(! type.equals("Bebidas")) {
            for (String tipo : anyType) {

                recetas = recetasApp.get(tipo);
                //Si no se ha cargado ya ese tipo de recetas, entonces hacemos un acceso a la Base de datos
                if (recetas.size() == 0)
                    RecipeRepository.getInstance().loadRecipesApp(recetas, tipo, "WHEEL");
                else
                    RotateViewModel.getInstance().setCompleted(tipo);
            }
        }else{
            recetas = recetasApp.get(type);
            if (recetas.size() == 0)
                RecipeRepository.getInstance().loadRecipesApp(recetas, type, "WHEEL");
            else
                RotateViewModel.getInstance().setCompleted(type);
        }
    }

}
