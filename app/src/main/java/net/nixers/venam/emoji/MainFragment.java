package net.nixers.venam.emoji;

import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

public class MainFragment extends Fragment {
    private EmojiListFragment emojiListFragment;

    public MainFragment() {}
    public MainFragment(EmojiListFragment m)
    {
        this.emojiListFragment = m;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.main_screen, container, false);
        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.ListOfEmotions);
        prepareLayout(container.getContext(), ll);
        return rootView;
    }

    private void prepareLayout(Context t, LinearLayout ll) {
        android.app.ActionBar.LayoutParams lp = new android.app.ActionBar.LayoutParams(
                android.app.ActionBar.LayoutParams.MATCH_PARENT,
                android.app.ActionBar.LayoutParams.WRAP_CONTENT
        );
        //fill the layout with all the emotions
        List<EmojiJson> allEmotions = MainActivity.allEmotions;
        for (EmojiJson j : allEmotions) {
            //prepare the button and card (under button - elevation)
            CardView card = createCardView(t);
            Button myButton = createButton(t,j.name);
            card.addView(myButton);
            //actually add the buttons to the layout
            ll.addView(card, lp);
        }
    }

    private CardView createCardView(Context t) {
        CardView card = new CardView(t);
        card.setCardBackgroundColor(0xFF4CAF50);
        card.setMaxCardElevation(10.0f);
        card.setCardElevation(7.0f);
        return card;
    }

    private Button createButton(Context t, String name) {
        Button myButton = new Button(t);
        myButton.setBackground(getResources().getDrawable(R.drawable.colored_button));
        myButton.setTextColor(0xFFFFFFFF);
        myButton.setText(name);
        //the callbacks
        myButton.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view){
                Button theClickedButton = (Button) view;
                String currentText = theClickedButton.getText().toString();
                String theRandomEmotion = getRandomEmotion(currentText);
                setToClipboard(view, theRandomEmotion);
                return true; //if consumed -- false otherwise
            }
        });
        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Button theClickedButton = (Button) view;
                String currentText = theClickedButton.getText().toString();
                MainActivity.mViewPager.setCurrentItem(1);
                emojiListFragment.changeLayout(currentText);
            }
        });
        return myButton;
    }

    private void setToClipboard(View view, String emotion){
        Toast.makeText(view.getContext(), emotion, Toast.LENGTH_SHORT).show();
        //copy it to the clipboard
        ClipData myClip = ClipData.newPlainText("emoji", emotion);
        MainActivity.myClipboard.setPrimaryClip(myClip);
        MainActivity.mVibrator.vibrate(300);
    }

    private String getRandomEmotion(String currentText) {
        //get all the available emotions
        List<String> emotions = null;
        for (EmojiJson j: MainActivity.allEmotions) {
            if (j.name.equalsIgnoreCase(currentText)) {
                emotions = j.emotions;
                break;
            }
        }
        //get a random emoji
        int theIndex = (int) Math.floor(Math.random()*emotions.size());
        String theRandomEmotion = emotions.get(theIndex);
        return theRandomEmotion;
    }

}
