package tomek.cukrzyca.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.R;


public class HelpFragment extends Fragment {

    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;

    public void onResume(){
        super.onResume();
        MainActivity.currentFragment = "helpFragment";
    }
    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        getActivity().setTitle("Pomoc");

        ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.lvExp);
        initData();
        ExpandableListAdapter listAdapter = new tomek.cukrzyca.ExpandableListAdapter(getContext(), listDataHeader, listHash);
        listView.setAdapter(listAdapter);

        return view;
    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("1) GDY NIE PRZYJĘTO INSULINY PRZEDPOSIŁKOWEJ");
        listDataHeader.add("2) JEŚLI NIE ISTNIEJE 100% PEWNOŚCI CZY PRZYJĘTO INSULINĘ PRZEDPOSIŁKOWĄ");
        listDataHeader.add("3) GDY NIE WSTRZYKNIĘTO INSULINY NPH");
        listDataHeader.add("4) JEŚLI NIE WSTRZYKNIĘTO ANALOGU INSULINY DŁUGO DZIAŁAJĄCEJ");

        List<String> list1= new ArrayList<>();
        list1.add("Jeśli:\n" +
                "•\tspostrzegłeś się CHWILĘ PO POSIŁKU \n" +
                "- wykonaj pomiar glukozy we krwi\n" +
                "- po czym możliwie najszybciej wstrzyknij ominięta dawkę\n" +
                "* można zmniejszyć dawkę o 1-2 j.\n" +
                "\n" +
                "•\tspostrzegłeś się 1-2 GODZINY PO POSIŁKU \n" +
                "- wykonaj pomiar glukozy we krwi\n" +
                "- po czym podaj połowę ominiętej dawki\n" +
                "\t \n" +
                "•\tspostrzegłeś się KILKA GODZIN PO POSIŁKU\n" +
                "- wykonaj pomiar glukozy we krwi\n" +
                "- w sytuacji występowania hiperglikemii, gdy nie jest planowany żaden posiłek w najbliższym czasie wstrzyknij dawkę korekcyjną o wartości 2-4j. bądź zwiększ dawkę leku przed następnym posiłkiem o 2-4j.\n");

        List<String> list2 = new ArrayList<>();
        list2.add("Jeśli:\n" +
                "•\tspostrzegłeś się PO POSIŁKU\n" +
                "- po upływie 2 godzin od posiłku wykonaj pomiar stężenia glukozy we krwi. Jeśli wynik jest zbyt wysoki oznacza ominięcie dawki.\n" +
                "W takim przypadku należy następnie postępować tak jak w sytuacji 1) GDY NIE PRZYJĘTO INSULINY PRZEDPOSIŁKOWEJ\n");


        List<String> list3 = new ArrayList<>();
        list3.add("Jeśli:\n" +
                "•\tspostrzegłeś się PRZED GODZINĄ 2:00 W NOCY\n" +
                "- wykonaj pomiar glukozy we krwi\n" +
                "- wstrzyknij 20-30% zaplanowanej dawki insuliny NPH, BĄDŹ zmniejsz dawkę o 1-2j. na każdą z godzin, które upłynęły od planowanego czasu wstrzyknięcia insuliny NPH\n" +
                "\n" +
                "Jeśli:\n" +
                "•\tspostrzegłeś się o TAKIEJ PORZE, OD KTÓREJ PO UPŁYWIE 5 GODZIN WSTAŁBYŚ \n" +
                "- wykonaj pomiar glukozy we krwi\n" +
                "- wstrzyknij  ⅓ lub ¼ dawki insuliny NPH lub dawkę korekcyjną insuliny przedposiłkowej\n" +
                "W przypadku, gdy rano także jest wstrzykiwana insulina NPH wykonaj wstrzyknięcie o wielkości dawki takiej samej jak na co dzień\n");

        List<String> list4 = new ArrayList<>();
        list4.add("Jeśli:\n" +
                "•\tspostrzegłeś się PO 2-3 GODZINACH\n" +
                "- wykonaj pomiar glukozy we krwi\n" +
                "- wstrzyknij planowaną dawkę insuliny długo działającej\n" +
                "* można dokonać redukcji dawki o 1-2 j.\n" +
                "\n" +
                "Jeśli\n" +
                "•\tspostrzegłeś się PO UPŁYWIE DŁUŻSZEGO OKRESU CZASU \n" +
                "- wykonaj pomiar glukozy we krwi\n" +
                "- wstrzyknij połowę planowanej dawkę insuliny długo działającej oraz na podstawie wartości zmierzonej glikemii wstrzyknij dawkę korekcyjną insuliny przedposiłkowej\n" +
                "\n" +
                "Jeśli\n" +
                "•\tspostrzegłeś się RANO, ŻE POMINĄŁEŚ PLANOWANĄ WCZORAJSZĄ WIECZORNĄ DAWKĘ INSULINY DŁUGODZIAŁAJĄCEJ \n" +
                "- w przypadku przyjmowania preparatu Levemir 2x w ciągu dnia: \n" +
                "wstrzyknij normalną poranną dawkę oraz na podstawie zmierzonej wartości glikemii podaj dawkę korekcyjną insuliny przedposiłkowej\n" +
                "- w przypadku przyjmowania preparatu Lantus lub Levemir 1 raz dziennie:\n" +
                "podaj połowę dawki \n");


        listHash.put(listDataHeader.get(0),list1);
        listHash.put(listDataHeader.get(1),list2);
        listHash.put(listDataHeader.get(2),list3);
        listHash.put(listDataHeader.get(3),list4);
    }

}
