package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Map;

/**
 * Created by dayouxia on 1/14/14.
 */
public class SudokuSolverFragment extends Fragment {


    private Grid mGrid = new Grid();
    private TableLayout mTableLayout;
    private static String TAG = "SUDOKU";
    private Button mSolveButton;
    private Button mClearButton;
    private static int  REQUEST_NUMBER = 1;
    private int mX;
    private int mY;


    private void updateUI(){
        for (int i = 0; i < mTableLayout.getChildCount()-1; i++){
            // 3*9
            TableRow oneRow = (TableRow) mTableLayout.getChildAt(i);

            //Log.v(TAG,"oneRow has "+ oneRow.getChildCount());
            for(int j = 0; j < oneRow.getChildCount(); j++){
                //3*3
                TableLayout smallRow = (TableLayout) oneRow.getChildAt(j);
                //Log.v(TAG,"smallRow has "+ smallRow.getChildCount());

                for(int k = 0; k < smallRow.getChildCount();k++){
                    //3*1
                    TableRow threeButton = (TableRow) smallRow.getChildAt(k);
                    //Log.v(TAG,"threeButton has "+ threeButton.getChildCount());

                    for(int l = 0; l < threeButton.getChildCount(); l++){
                        Button oneButton = (Button) threeButton.getChildAt(l);
                        int number = mGrid.array[3*i+k][3*j+l];
                        if(number == 0){
                            oneButton.setText("");
                        } else{
                            oneButton.setText(number+"");
                        }

                    }
                }



            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) return;

        if(requestCode == REQUEST_NUMBER ){
            int newNumber = data.getIntExtra(NumberPickerFragment.EXTRA_NUM,-1);
            mGrid.array[mX][mY] = newNumber;
            updateUI();






                }








    }

    private void setupButton(){

        View.OnClickListener numberListner = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button z = (Button)view;
                z.setText("+");

            }
        };
        for (int i = 0; i < mTableLayout.getChildCount()-1; i++){
            // 3*9
            TableRow oneRow = (TableRow) mTableLayout.getChildAt(i);

            //Log.v(TAG,"oneRow has "+ oneRow.getChildCount());
            for(int j = 0; j < oneRow.getChildCount(); j++){
                //3*3
                TableLayout smallRow = (TableLayout) oneRow.getChildAt(j);
                //Log.v(TAG,"smallRow has "+ smallRow.getChildCount());

                for(int k = 0; k < smallRow.getChildCount();k++){
                    //3*1
                    TableRow threeButton = (TableRow) smallRow.getChildAt(k);
                    //Log.v(TAG,"threeButton has "+ threeButton.getChildCount());

                    for(int l = 0; l < threeButton.getChildCount(); l++){
                        Button oneButton = (Button) threeButton.getChildAt(l);
                        final int x = 3*i+k;
                        final int y = 3*j+l;

                        oneButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mX = x;
                                mY = y;
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                NumberPickerFragment dialog = NumberPickerFragment.newInstance();
                                dialog.setTargetFragment(SudokuSolverFragment.this,REQUEST_NUMBER);
                                dialog.show(fm,"zzz");
                            }
                        });
                        int number = mGrid.array[3*i+k][3*j+l];


                    }
                }



            }
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sudoku_solver_fragment,container,false);

        mTableLayout = (TableLayout)v.findViewById(R.id.sudoku_fragment);
        updateUI();

        mSolveButton = (Button)v.findViewById(R.id.solve_button);
        mSolveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SudokuSolver.solveSudoku(mGrid);
                updateUI();

            }
        });



        mClearButton = (Button)v.findViewById(R.id.clear_button);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGrid.clearGrid();
                updateUI();
            }
        });

        setupButton();








        return v;
    }



}
