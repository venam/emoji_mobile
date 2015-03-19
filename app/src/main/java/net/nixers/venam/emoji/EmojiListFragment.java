package net.nixers.venam.emoji;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;


public class EmojiListFragment extends Fragment {
    private LinearLayout linearLayout;
    private View rootView;
    private MainActivity mainActivity;
    private Context context;
    public EmojiListFragment() {}
    public EmojiListFragment(MainActivity m) {
        this.mainActivity = m;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_emoji_particular, container, false);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.linearListParticularEmo);
        context=container.getContext();
        this.setEmotionList("happy");
        return rootView;
    }

    private CardView createCardView() {
        CardView card = new CardView(context);
        card.setCardBackgroundColor(0xE5E5E5);
        card.setMaxCardElevation(10.0f);
        card.setCardElevation(7.0f);
        return card;
    }

    private void setEmotionList(String emotion) {
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT
        );

        String message = emotion;
        //fill the layout with all the emotions
        List<String> emotions = null;
        for (EmojiJson j : MainActivity.allEmotions) {
            //get all the available emotions
            if (j.name.equalsIgnoreCase(message)) {
                emotions = j.emotions;
                break;
            }
        }
        for (String j : emotions) {
            CardView card = createCardView();
            Button myButton = createButton(rootView, j);
            card.addView(myButton);
            linearLayout.addView(card, lp);
        }
    }

    private Button createButton(View rootView, String j) {
        Button myButton = new Button(rootView.getContext());
        myButton.setText(j);
        myButton.setBackground(getResources().getDrawable(R.drawable.colored_button2));
        //myButton.setTextColor(0xFFFFFF);
        myButton.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                Button theClickedButton = (Button) view;
                String currentText = theClickedButton.getText().toString();
                Toast.makeText(view.getContext(), "Added "+currentText+" To favorites", Toast.LENGTH_SHORT).show();
                mainActivity.addEmotion(currentText);
                return true; //if consumed -- false otherwise
            }
        });
        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.mVibrator.vibrate(300);
                // the actual text of the button *is* the emoji we want
                Button theClickedButton = (Button) view;
                String currentText = theClickedButton.getText().toString();
                Toast.makeText(view.getContext(), currentText, Toast.LENGTH_SHORT).show();
                //copy it to the clipboard
                ClipData myClip = ClipData.newPlainText("emoji", currentText);
                MainActivity.myClipboard.setPrimaryClip(myClip);

            }
        });
        return myButton;
    }

    public boolean changeLayout(String emotion) {
        linearLayout.removeAllViews();
        setEmotionList(emotion);
        mainActivity.setActionBarTitle("Emoji - "+emotion);
        return true;
    }
}
