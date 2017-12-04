package inc.appscode0.landvaluationapplication;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import inc.appscode0.landvaluationapplication.nearme.SearchLocbyType;


public class MainActivity extends AppCompatActivity {
    private Spinner spinner0,spinner1, spinner2, spinner3, spinner4, spinner5, spinner6, spinner7, spinner8,spinner9;
    private Button calculate,resetButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addItemsOnSpinner2();
        addItemsOnSpinner3();
        addItemsOnSpinner4();
        addItemsOnSpinner5();
        addItemsOnSpinner6();
        addItemsOnSpinner7();
        addItemsOnSpinner8();
        addItemsOnSpinner9();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();
        addListenerOnSpinnerItemSelection1();

        //reset button
        Button reset=(Button)findViewById(R.id.resetButton);
        reset.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                spinner0.setSelection(0);
                spinner1.setSelection(0);
                spinner2.setSelection(0);
                spinner3.setSelection(0);
                spinner4.setSelection(0);
                spinner5.setSelection(0);
                spinner6.setSelection(0);
                spinner7.setSelection(0);
                spinner8.setSelection(0);
                spinner9.setSelection(0);

            }
        });

        final TextView locationLink = (TextView) findViewById(R.id.SearchLocation);

        locationLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent registerIntent= new Intent(MainActivity.this,SearchLocbyType.class);
                MainActivity.this.finish();
                MainActivity.this.startActivity(registerIntent);

            }
        });
        spinner0.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this,spinner0.getSelectedItem().toString() , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        try
        {

            Intent intent = getIntent();
            String json_data = intent.getStringExtra("json_data");
            Log.d("json_data", json_data);

            JSONArray  jsonArray = new JSONArray(json_data);
            Double[] data_school = new Double[jsonArray.length()];
            Double[] data_hospital = new Double[jsonArray.length()];
            Double[] data_university= new Double[jsonArray.length()];


            for(int i=0; i<jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String type= jsonObject.getString("type");
                String dis = jsonObject.getString("distance");

                if(type.equals("school"))
                {
                    data_school[i]=Double.parseDouble(dis);
                    Double without_null []=removeNull(data_school);
                    Arrays.sort(without_null);
                    Double smallest=  without_null[0];
                    set_data(type, String.valueOf(smallest));

                }


                if(type.equals("university"))
                {
                    data_university[i]=Double.parseDouble(dis);
                    Double without_null []=removeNull(data_university);
                    Arrays.sort(without_null);
                    Double smallest=  without_null[0];
                    set_data(type, String.valueOf(smallest));
                }


                if(type.equals("hospital"))
                {
                    data_hospital[i]=Double.parseDouble(dis);
                    Double without_null []=removeNull(data_hospital);
                    Arrays.sort(without_null);
                    Double smallest=  without_null[0];
                    set_data(type, String.valueOf(smallest));

                }


            }

//            Toast.makeText(this, String.valueOf(data_school[0]), Toast.LENGTH_SHORT).show();
//
//
//            if(data_school!=null)
//            {
//                Arrays.sort(data_school);
//                Double smallest = data_school[0];
//                Toast.makeText(this, String.valueOf(smallest), Toast.LENGTH_SHORT).show();
//                for(int i=0; i<jsonArray.length(); i++)
//                {
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    String type= jsonObject.getString("type");
//                    String dis = jsonObject.getString("distance");
//
//                    if(String.valueOf(smallest).equals(dis))
//                    {
//                        set_data(type, String.valueOf(smallest));
//                    }
//                }
//            }
//
//
//            if(data_university!=null)
//            {
//                Arrays.sort(data_university);
//                Double smallest = data_university[0];
//                for(int i=0; i<jsonArray.length(); i++)
//                {
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    String type= jsonObject.getString("type");
//                    String dis = jsonObject.getString("distance");
//
//                    if(String.valueOf(smallest).equals(dis))
//                    {
//                        set_data(type, String.valueOf(smallest));
//                    }
//                }
//            }
//
//
//
//
//            if(data_hospital.length!=0)
//            {
//                Arrays.sort(data_hospital);
//                Double smallest = data_hospital[0];
//                for(int i=0; i<jsonArray.length(); i++)
//                {
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    String type= jsonObject.getString("type");
//                    String dis = jsonObject.getString("distance");
//
//                    if(String.valueOf(smallest).equals(dis))
//                    {
//                        set_data(type, String.valueOf(smallest));
//                    }
//                }
//            }









        }
        catch (Exception ex)
        {
            Toast.makeText(this, String.valueOf(ex), Toast.LENGTH_SHORT).show();
            Log.d("ex", String.valueOf(ex));
        }


        //  reset */
    }

    public Double[] removeNull(Double[] a)
    {
        ArrayList<Double> removedNull = new ArrayList<Double>();
        for (Double str : a)
            if (str != null)
                removedNull.add(str);
        return removedNull.toArray(new Double[0]);
    }

    public void set_data(final String placetype, final String distance)
    {
        final Double dis=  Double.parseDouble(distance);
        switch (placetype){
            case "school":
                Log.d("data", "school distance ->" +distance);
                spinner8.setSelection(2);
                if(dis<100)
                {
                    spinner9.setSelection(1);
                }
                else if(dis<300)
                {
                    spinner9.setSelection(2);
                }
                else if(dis<1000)
                {
                    spinner9.setSelection(3);
                }
                else if(dis<2000)
                {
                    spinner9.setSelection(4);
                }
                else if(dis>2000)
                {
                    spinner9.setSelection(5);
                }
                break;
            case "university":
                Log.d("data", "university distance ->" +distance);
                spinner8.setSelection(1);
                if(dis<100)
                {
                    spinner9.setSelection(1);
                }
                else if(dis<300)
                {
                    spinner9.setSelection(2);
                }
                else if(dis<1000)
                {
                    spinner9.setSelection(3);
                }
                else if(dis<2000)
                {
                    spinner9.setSelection(4);
                }
                else if(dis>2000)
                {
                    spinner9.setSelection(5);
                }
                break;
            case "hospital":
                Log.d("data", "hospital distance ->" +distance);
                spinner6.setSelection(1);
                if(dis<100)
                {
                    spinner7.setSelection(1);
                }
                else if(dis<300)
                {
                    spinner7.setSelection(2);
                }
                else if(dis<1000)
                {
                    spinner7.setSelection(3);
                }
                else if(dis<2000)
                {
                    spinner7.setSelection(4);
                }
                else if(dis>2000)
                {
                    spinner7.setSelection(5);
                }
                break;




        }
    }
    // add items into spinner dynamically
    public void addItemsOnSpinner2() {

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        List<String> list = new ArrayList<String>();
        list.add("Presence Of main Road");
        list.add("Super Highway");
        list.add("Normal Highway");
        list.add("Normal Road");
        list.add("Murram Road");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }
    public void addItemsOnSpinner3() {

        spinner3 = (Spinner) findViewById(R.id.spinner3);
        List<String> list = new ArrayList<String>();
        list.add("Proximity from Main Road");
        list.add("0-100m");
        list.add("100-300m");
        list.add("300-1000m");
        list.add("1000-2000");
        list.add(">2000m");
        list.add(">20000m");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(dataAdapter);
    }
    public void addItemsOnSpinner4() {

        spinner4 = (Spinner) findViewById(R.id.spinner4);
        List<String> list = new ArrayList<String>();
        list.add("Railway Availability");
        list.add("Standard Gauge Railway");
        list.add("Normal Railway");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(dataAdapter);
    }
    public void addItemsOnSpinner5() {

        spinner5 = (Spinner) findViewById(R.id.spinner5);
        List<String> list = new ArrayList<String>();
        list.add("Proximity from Railway");
        list.add("0-100m");
        list.add("100-300m");
        list.add("300-1000m");
        list.add("1000-2000");
        list.add(">2000m");
        list.add(">20000m");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(dataAdapter);
    }
    public void addItemsOnSpinner6()
    {

        spinner6 = (Spinner) findViewById(R.id.spinner6);
        List<String> list = new ArrayList<String>();
        list.add("Hospital Availability");
        list.add("Major Hospital");
        list.add("Minor Hospital");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner6.setAdapter(dataAdapter);
    }
    public void addItemsOnSpinner7()
    {


        spinner7 = (Spinner) findViewById(R.id.spinner7);
        List<String> list = new ArrayList<String>();
        list.add("Proximity from Hospital");
        list.add("0-100m");
        list.add("100-300m");
        list.add("300-1000m");
        list.add("1000-2000");
        list.add(">2000m");
        list.add(">20000m");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner7.setAdapter(dataAdapter);
    }
    public void addItemsOnSpinner8() {

        spinner8 = (Spinner) findViewById(R.id.spinner8);
        List<String> list = new ArrayList<String>();
        list.add("School Availability");
        list.add("University");
        list.add("High School");
        list.add("Primary School");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner8.setAdapter(dataAdapter);
    }
    public void addItemsOnSpinner9()
    {

        spinner9 = (Spinner) findViewById(R.id.spinner9);
        List<String> list = new ArrayList<String>();
        list.add("Proximity from School");
        list.add("0-100m");
        list.add("100-300m");
        list.add("300-1000m");
        list.add("1000-2000");
        list.add(">2000m");
        list.add(">20000m");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner9.setAdapter(dataAdapter);
    }
    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }
    public void addListenerOnSpinnerItemSelection1() {
        spinner0 = (Spinner) findViewById(R.id.spinner0);
        spinner0.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    // get the selected dropdown list value
    double Landratio;
    int RegVal = 484800000;
    int price= 0;
    double LandValue;
    public void addListenerOnButton() {
        spinner0 = (Spinner) findViewById(R.id.spinner0);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        spinner5 = (Spinner) findViewById(R.id.spinner5);
        spinner6 = (Spinner) findViewById(R.id.spinner6);
        spinner7 = (Spinner) findViewById(R.id.spinner7);
        spinner8 = (Spinner) findViewById(R.id.spinner8);
        spinner9 = (Spinner) findViewById(R.id.spinner9);
        calculate = (Button) findViewById(R.id.btnSubmit);

        calculate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                String County=spinner0.getSelectedItem().toString();
                double percentage_county=county(County);
                String Size=spinner1.getSelectedItem().toString();
                double percentage_size=size(Size);
                String MR=spinner2.getSelectedItem().toString();
                double percentage_road=road(MR);
                String DMR=spinner3.getSelectedItem().toString();
                double percentage_Rdist=roadD(DMR);
                String RA=spinner4.getSelectedItem().toString();
                double percentage_rail=railway(RA);
                String DRA=spinner5.getSelectedItem().toString();
                double percentage_raildist=railwayD(DRA);
                String HA=spinner6.getSelectedItem().toString();
                double percentage_hosp=hospital(HA);
                String DHA=spinner7.getSelectedItem().toString();
                double percentage_hospD=hospitalD(DHA);
                String SA=spinner8.getSelectedItem().toString();
                double percentage_school=school(SA);
                String DSA=spinner9.getSelectedItem().toString();
                double percentage_schoold=schoolD(DSA);
/*
        Double.parseDouble(County);
        Double.parseDouble(Size);
        Double.parseDouble(MR);
        Double.parseDouble(DMR);
        Double.parseDouble(RA);
        Double.parseDouble(DRA);
        Double.parseDouble(HA);
        Double.parseDouble(DHA);
        Double.parseDouble(SA);
        Double.parseDouble(DSA);
*/



                Landratio=percentage_county*percentage_road*percentage_Rdist*percentage_rail*percentage_raildist*percentage_hosp*percentage_hospD*percentage_school*percentage_schoold;
                LandValue= price+(Landratio*(percentage_size*RegVal));


             //   Toast.makeText(MainActivity.this, String.valueOf(LandValue), Toast.LENGTH_SHORT).show();

//                Toast.makeText(MainActivity.this,
//                        "Values Selected : " +
//                                "\nLandSize        : " + String.valueOf(spinner1.getSelectedItem()) +
//                                "\nCounty          : " + String.valueOf(spinner0.getSelectedItem()) +
//                                "\nRoad            : " + String.valueOf(spinner2.getSelectedItem())+
//                                "\nRoadDistance    : " + String.valueOf(spinner3.getSelectedItem())+
//                                "\nRailway         : " + String.valueOf(spinner4.getSelectedItem())+
//                                "\nRailwayDistance : " + String.valueOf(spinner5.getSelectedItem())+
//                                "\nHospital        : " + String.valueOf(spinner6.getSelectedItem())+
//                                "\nHospitalDistance: " + String.valueOf(spinner7.getSelectedItem())+
//                                "\nSchool          : " + String.valueOf(spinner8.getSelectedItem())+
//                                "\nSchoolDistance  : " + String.valueOf(spinner9.getSelectedItem()),
//                        Toast.LENGTH_SHORT).show();
               startActivity(new Intent(MainActivity.this, LandValueActivity.class)
               .putExtra("value", String.valueOf(LandValue)));


           // finish();


            }

        });


    }
    double percentage;
    public Double county(String selected) {
        //Spinner0 selected
        //   String selected=spinner0.getSelectedItem().toString();

        if (selected.equals("Nairobi")) {
            percentage = 1;
        } else if (selected.equals("Nakuru")) {
            percentage = 0.75;
        } else if (selected.equals("Nanyuki")) {
            percentage = 0.8;
        } else if (selected.equals("Nyeri")) {
            percentage = 0.54;
        } else if (selected.equals("Nyahururu")) {
            percentage = 0.48;
        }
        return percentage;
    }
    public Double size(String selected) {
        //Spinner1 selected
        //selected = spinner1.getSelectedItem().toString();

        if(selected.equals("1/4 Acre"))
        {
            percentage=0.25;
        }
        else if(selected.equals("1/2 Acre"))
        {
            percentage=0.5;
        }
        else if(selected.equals("3/4 Acre"))
        {
            percentage=0.75;
        }
        else if(selected.equals("1 Acre"))
        {
            percentage=1;
        }
        else if(selected.equals("Plot"))
        {
            percentage=0.68;
        }
        return percentage;
    }
    public Double road(String selected) {
        //Spinner2 selected
        //selected = spinner2.getSelectedItem().toString();

        if(selected.equals("Super Highway"))
        {
            percentage=1;
        }
        else if(selected.equals("Normal Highway"))
        {
            percentage=0.85;
        }
        else if(selected.equals("Normal Road"))
        {
            percentage=0.52;
        }
        else if(selected.equals("Murram Road"))
        {
            percentage=0.1;
        }
        return percentage;
    }
    public Double roadD(String selected) {
        //Spinner3 selected
      //  selected = spinner3.getSelectedItem().toString();

        if(selected.equals("0-100m"))
        {
            percentage=1;
        }
        else if(selected.equals("100-300m"))
        {
            percentage=0.92;
        }
        else if(selected.equals("300-1000m"))
        {
            percentage=0.86;
        }
        else if(selected.equals("1000-2000m"))
        {
            percentage=0.74;
        }
        else if(selected.equals(">2000m"))
        {
            percentage=0.55;
        }
        else if(selected.equals(">20000m"))
        {
            percentage=0.3;
        }
        return percentage;
    }
    public Double railway(String selected) {
        //Spinner4 selected
       // selected = spinner4.getSelectedItem().toString();

        if(selected.equals("Standard Gauge Railway"))
        {
            percentage=1;
        }
        else if(selected.equals("Normal Railway"))
        {
            percentage=0.84;
        }
        return percentage;
    }
    public Double railwayD(String selected) {
        //Spinner5 selected
      //  selected = spinner5.getSelectedItem().toString();

        if(selected.equals("0-100m"))
        {
            percentage=1;
        }
        else if(selected.equals("100-300m"))
        {
            percentage=0.92;
        }
        else if(selected.equals("300-1000m"))
        {
            percentage=0.86;
        }
        else if(selected.equals("1000-2000m"))
        {
            percentage=0.74;
        }
        else if(selected.equals(">2000m"))
        {
            percentage=0.55;
        }
        else if(selected.equals(">20000m"))
        {
            percentage=0.1;
        }
        return percentage;
    }
    public Double hospital(String selected) {
        //Spinner6 selected
     //   selected = spinner6.getSelectedItem().toString();

        if(selected.equals("Major Hospital"))
        {
            percentage=1;
        }
        else if(selected.equals("Minor Hospital"))
        {
            percentage=0.85;
        }
        return percentage;
    }
    public Double hospitalD(String selected) {
        //Spinner7 selected
        //selected = spinner7.getSelectedItem().toString();

        if(selected.equals("0-100m"))
        {
            percentage=1;
        }
        else if(selected.equals("100-300m"))
        {
            percentage=0.92;
        }
        else if(selected.equals("300-1000m"))
        {
            percentage=0.86;
        }
        else if(selected.equals("1000-2000m"))
        {
            percentage=0.74;
        }
        else if(selected.equals(">2000m"))
        {
            percentage=0.55;
        }
        else if(selected.equals(">20000m"))
        {
            percentage=0.1;
        }
        return percentage;
    }
    public Double school(String selected) {
        //Spinner8 selected
     //   selected = spinner8.getSelectedItem().toString();

        if(selected.equals("University"))
        {
            percentage=1;
        }
        else if(selected.equals("High School"))
        {
            percentage=0.85;
        }
        else if(selected.equals("Primary School"))
        {
            percentage=0.77;
        }
        return percentage;
    }
    public Double schoolD(String selected) {
        //Spinner9 selected
      //  selected = spinner9.getSelectedItem().toString();

        if(selected.equals("0-100m"))
        {
            percentage=1;
        }
        else if(selected.equals("100-300m"))
        {
            percentage=0.92;
        }
        else if(selected.equals("300-1000m"))
        {
            percentage=0.86;
        }
        else if(selected.equals("1000-2000m"))
        {
            percentage=0.74;
        }
        else if(selected.equals(">2000m"))
        {
            percentage=0.55;
        }
        else if(selected.equals(">20000m"))
        {
            percentage=0.1;
        }
        return percentage;
    }

}
