package com.example.admin.whiteout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    final CharSequence[] sizes = {
            "5x5", "7x7", "9x9", "cancel"
    };
    Button b1;
    Button b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(new MainView(this, new WhiteOut(10)));
        b1 = (Button) findViewById(R.id.reset);
        b2 = (Button) findViewById(R.id.show_level_dialog);
        b2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLevelDialog(v);
//                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                        builder.setTitle(R.string.dialog_title);
//                        builder.setItems(sizes, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int item) {
//                                switch (item) {
//                                    case 0:
//                                        MainView.dim = 5;
//                                        resetModel();                                        break;
//                                    case 1:
//                                        MainView.dim = 7;
//                                        resetModel();                                        break;
//                                    case 2:
//                                        MainView.dim = 9;
//                                        resetModel();                                        break;
//                                    default:
//                                        break;
//                                }
//                            }
//                        });
//                        AlertDialog alert = builder.create();
//                        alert.show();
                    }
                });
    }


    // Reset the game
    public void resetModel(View v) {
        MainView mv = (MainView) findViewById(R.id.whiteout);
        mv.getModel().reset(MainView.dim);
        mv.postInvalidate();
    }

    public void showLevelDialog(View v) {
        final CharSequence[] sizes = {
                "5x5", "7x7", "9x9", "cancel"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title)
                .setItems(sizes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                MainView.dim = 5;
                                resetModel(null);
                                break;
                            case 1:
                                MainView.dim = 7;
                                resetModel(null);
                                break;
                            case 2:
                                MainView.dim = 9;
                                resetModel(null);
                                break;
                            case 3:
                                dialog.cancel();
                                break;
                            default:
                                dialog.cancel();
                                break;
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
