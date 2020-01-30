package roid.berlin.memoapp.android.co.Fragments;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import roid.berlin.memoapp.android.co.Activitys.MainActivity;
import roid.berlin.memoapp.android.co.Activitys.NoteAddActivity;
import roid.berlin.memoapp.android.co.Activitys.PatternLockActivity;
import roid.berlin.memoapp.android.co.Activitys.PinLockActivity;
import roid.berlin.memoapp.android.co.Other.ViewAnimation;
import roid.berlin.memoapp.android.co.R;

public class SettingFragment extends Fragment {

    SwitchCompat switchCompat;
    FrameLayout settingFrameLayout;
    ImageButton btn_backToMain;
    LinearLayout LY_theme, LY_font;
    TextView txt_SF_theme, txt_SF_font;
    CardView careViewSetting;

    LinearLayout non_pass,pin_pas,Pattern_pass,finger_pas;

    RadioGroup rg_changFont;
    RadioButton rb_Bnazanin, rb_Byekan;


    private NestedScrollView nested_scroll_view;
    private ImageButton bt_toggle_input, bt_toggle_text, bt_toggle_pass;
    private Button bt_save_input, bt_hide_input, btn_hide_text, btn_save_text,
            btn_hide_pass, btn_save_pass;
    private View lyt_expand_input, lyt_expand_text, lyt_expand_pass;
    public static boolean mIsNightMode = false;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SettingFragment() {
        // Required empty public constructor
    }


    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        careViewSetting = view.findViewById(R.id.cardViewSetting);
        settingFrameLayout = view.findViewById(R.id.setting_frameLayout);
        LY_theme = view.findViewById(R.id.LY_theme);
        LY_font = view.findViewById(R.id.LY_font);
        txt_SF_theme = view.findViewById(R.id.txt_SF_theme);
        txt_SF_font = view.findViewById(R.id.txt_SF_font);

        rg_changFont = view.findViewById(R.id.rg_changFont);
        rb_Bnazanin = view.findViewById(R.id.B_NAZANIN);
        rb_Byekan = view.findViewById(R.id.B_Yekan);

        final Typeface font_Bnazanin = Typeface.createFromAsset(getActivity().getAssets(), "fonts/B-NAZANIN.TTF");
        Typeface font_Byekan = Typeface.createFromAsset(getActivity().getAssets(), "fonts/B Yekan+ Bold.ttf");

        rb_Bnazanin.setTypeface(font_Bnazanin);
        rb_Byekan.setTypeface(font_Byekan);

        rg_changFont.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (rg_changFont.getCheckedRadioButtonId()) {

                    case R.id.B_NAZANIN:
//                        bt_hide_input.setTypeface(font_Bnazanin);
//                        bt_save_input.setTypeface(font_Bnazanin);
//                        btn_hide_text.setTypeface(font_Bnazanin);
//                        ((MainActivity)getActivity()).changFont_Bnazanin();

                        ((MainActivity) getActivity()).customeFont(getActivity(), "SERIF", "fonts/B-NAZANIN.TTF");

                        break;
                    case R.id.B_Yekan:
                        ((MainActivity) getActivity()).customeFontTwo(getActivity(), "SERIF", "fonts/B Yekan+ Bold.ttf");
                        break;
                    default:
                        break;
                }
            }
        });
        btn_backToMain = view.findViewById(R.id.settingFragment_btn_back);
        btn_backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        switchCompat = view.findViewById(R.id.switch_dark_mode);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mIsNightMode = b;

                if (mIsNightMode) {
                    ((MainActivity) getActivity()).getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                   // ((MainActivity) getActivity()).mainCardsViewBackground();
                    careViewSetting.setCardBackgroundColor(Color.parseColor("#353131"));
                    settingFrameLayout.setBackgroundColor(Color.parseColor("#353131"));
                    LY_theme.setBackgroundColor(Color.parseColor("#443F3F"));
                    LY_font.setBackgroundColor(Color.parseColor("#443F3F"));
                    txt_SF_theme.setTextColor(Color.parseColor("#FFFFFF"));
                    txt_SF_font.setTextColor(Color.parseColor("#FFFFFF"));

                    // ((NoteAddActivity) getActivity()).noteCardsViewBackground();

                } else {
                    ((MainActivity) getActivity()).getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);



                }
            }
        });


        bt_toggle_input = view.findViewById(R.id.bt_toggle_input);
        bt_hide_input = view.findViewById(R.id.bt_hide_input);
        bt_save_input = view.findViewById(R.id.bt_save_input);
        lyt_expand_input = view.findViewById(R.id.lyt_expand_input);
        nested_scroll_view = view.findViewById(R.id.nested_scroll_view);
        lyt_expand_input.setVisibility(View.GONE);

        bt_toggle_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInput(bt_toggle_input);
            }
        });

        bt_hide_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInput(bt_toggle_input);
            }
        });

        bt_save_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bt_toggle_text = view.findViewById(R.id.bt_toggle_text);
        btn_hide_text = view.findViewById(R.id.bt_hide_text);
        lyt_expand_text = view.findViewById(R.id.lyt_expand_text);
        lyt_expand_text.setVisibility(View.GONE);

        bt_toggle_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText(bt_toggle_text);
            }
        });

        btn_hide_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText(bt_toggle_text);
            }
        });

        bt_toggle_pass = view.findViewById(R.id.bt_toggle_pass);
        btn_hide_pass = view.findViewById(R.id.bt_hide_pass);
        btn_save_pass = view.findViewById(R.id.bt_save_pass);
        lyt_expand_pass = view.findViewById(R.id.lyt_expand_pass);
        lyt_expand_pass.setVisibility(View.GONE);

        bt_toggle_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionPass(bt_toggle_pass);
            }
        });

        btn_hide_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionPass(bt_toggle_pass);
            }
        });

        pin_pas=view.findViewById(R.id.pin_pass);
        pin_pas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(getActivity(), PinLockActivity.class);
                getActivity().startActivity(intent);
            }
        });
        Pattern_pass = view.findViewById(R.id.pattern_pass);
        Pattern_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), PatternLockActivity.class);
                getActivity().startActivity(intent);
            }
        });

    }

    private void toggleSectionPass(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_pass, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    nestedScrollTo(nested_scroll_view, lyt_expand_pass);
                }
            });

        } else {
            ViewAnimation.collapse(lyt_expand_pass);
        }
    }

    private void toggleSectionInput(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_input, new ViewAnimation.AnimListener() {
                public void onFinish() {
                    nestedScrollTo(nested_scroll_view, lyt_expand_input);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_input);
        }
    }

    private void toggleSectionText(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_text, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    nestedScrollTo(nested_scroll_view, lyt_expand_text);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_text);
        }
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    public static void nestedScrollTo(final NestedScrollView nested, final View targetView) {
        nested.post(new Runnable() {
            @Override
            public void run() {
                nested.scrollTo(500, targetView.getBottom());
            }
        });
    }


}
