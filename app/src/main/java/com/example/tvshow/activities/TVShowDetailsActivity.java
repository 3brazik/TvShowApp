package com.example.tvshow.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.tvshow.R;

import com.example.tvshow.adapters.EpisodeAdapter;
import com.example.tvshow.adapters.ImageSliderAdapter;
import com.example.tvshow.databinding.ActivityTvshowDetailsBinding;
import com.example.tvshow.databinding.LayoutEpisodesButtonSheetBinding;
import com.example.tvshow.models.TVShow;
import com.example.tvshow.viewmodels.TVShowDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import io.reactivex.schedulers.Schedulers;

public class TVShowDetailsActivity extends AppCompatActivity {
    private ActivityTvshowDetailsBinding binding;
    private TVShowDetailsViewModel viewModel;
    private BottomSheetDialog episodeBottomSheetDialog;
    private LayoutEpisodesButtonSheetBinding layoutEpisodesButtonSheetBinding;
    private TVShow tvShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details);
        doInitialization();

    }

    private void doInitialization() {
        viewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        tvShow = (TVShow) getIntent().getSerializableExtra("tvShow");
        getTVShowDetails();

    }

    private void getTVShowDetails() {
        binding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());

        viewModel.getTVShowDetails(tvShowId).observe(this,
                tvShowDetailsResponse -> {
                    binding.setIsLoading(false);
                    if (tvShowDetailsResponse.getTvShowDetails() != null) {
                        if (tvShowDetailsResponse.getTvShowDetails().getPictures() != null) {
                            loadImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                        }
                        binding.setTvShowImageURL(
                                tvShowDetailsResponse.getTvShowDetails().getImage_path()
                        );
                        binding.imageTvsShow.setVisibility(View.VISIBLE);
                        binding.setDescription(
                                String.valueOf(HtmlCompat.fromHtml(
                                        tvShowDetailsResponse.getTvShowDetails().getDescription(),
                                        HtmlCompat.FROM_HTML_MODE_LEGACY
                                ))
                        );
                        binding.textDescription.setVisibility(View.VISIBLE);
                        binding.readMore.setVisibility(View.VISIBLE);
                        binding.readMore.setOnClickListener(v -> {
                            if (binding.readMore.getText().toString().equals("Read More")) {
                                binding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                binding.textDescription.setEllipsize(null);
                                binding.readMore.setText(R.string.read_less);

                            } else {
                                binding.textDescription.setMaxLines(4);
                                binding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                binding.readMore.setText(R.string.read_more);
                            }
                        });
                        binding.setRating(
                                String.format(Locale.getDefault(),
                                        "%.2f",
                                        Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating()))
                        );
                        if (tvShowDetailsResponse.getTvShowDetails().getGenres() != null) {
                            binding.setGenres(tvShowDetailsResponse.getTvShowDetails().getGenres()[0]);
                        } else {
                            binding.setGenres("N/A");
                        }
                        binding.setRuntime(tvShowDetailsResponse.getTvShowDetails().getRuntime() + "Min");
                        binding.viewDivider1.setVisibility(View.VISIBLE);
                        binding.layoutMisc.setVisibility(View.VISIBLE);
                        binding.viewDivider2.setVisibility(View.VISIBLE);
                        binding.buttonWebsite.setOnClickListener(v -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                            startActivity(intent);
                        });
                        binding.buttonWebsite.setVisibility(View.VISIBLE);
                        binding.buttonEpisodes.setVisibility(View.VISIBLE);
                        binding.buttonEpisodes.setOnClickListener(v -> {
                            if (episodeBottomSheetDialog == null) {
                                episodeBottomSheetDialog = new BottomSheetDialog(TVShowDetailsActivity.this);
                                layoutEpisodesButtonSheetBinding = DataBindingUtil.inflate(LayoutInflater.from(TVShowDetailsActivity.this)
                                        , R.layout.layout_episodes_button_sheet, findViewById(R.id.episodesContainer), false);

                                episodeBottomSheetDialog.setContentView(layoutEpisodesButtonSheetBinding.getRoot());
                                layoutEpisodesButtonSheetBinding.episodesRecyclerView.
                                        setAdapter(new EpisodeAdapter(tvShowDetailsResponse.getTvShowDetails().getEpisodes()));
                                layoutEpisodesButtonSheetBinding.textTitle.setText(String.format("Episodes | %s", tvShow.getName()));
                                layoutEpisodesButtonSheetBinding.imageClose.setOnClickListener(v1 -> episodeBottomSheetDialog.dismiss());
                            }
                            //--------Optional section start //
                            FrameLayout frameLayout = episodeBottomSheetDialog.
                                    findViewById(com.google.android.material.R.id.design_bottom_sheet);
                            if (frameLayout != null) {
                                BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                                bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                            }
                            // Optional Section end ------ //
                            episodeBottomSheetDialog.show();
                        });
                        binding.imageWatchList.setOnClickListener(v ->
                                new CompositeDisposable().add(viewModel.addToWatchList(tvShow).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                                            binding.imageWatchList.setImageResource(R.drawable.ic_added);
                                            Toast.makeText(getApplicationContext(), "Added to Watchlist", Toast.LENGTH_SHORT).show();
                                        })
                                ));
                        binding.imageWatchList.setVisibility(View.VISIBLE);
                        loadBasicTVShowDetails();
                    }
                }
        );
    }

    private void loadImageSlider(String[] sliderImage) {
        binding.slideViewPager.setOffscreenPageLimit(1);
        binding.slideViewPager.setAdapter(new ImageSliderAdapter(sliderImage));
        binding.slideViewPager.setVisibility(View.VISIBLE);
        binding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicators(sliderImage.length);
        binding.slideViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });
    }

    private void setupSliderIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.background_slider_indicator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            binding.layoutSliderIndicators.addView(indicators[i]);
        }
        binding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position) {
        int childCount = binding.layoutSliderIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) binding.layoutSliderIndicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.background_slider_indicator_active));
            } else
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.background_slider_indicator_inactive));
        }
    }

    private void loadBasicTVShowDetails() {
        binding.setTvShowName(tvShow.getName());
        binding.setStatus(tvShow.getStatus());
        binding.setNetworkCountry(tvShow.getNetwork() + "(" + tvShow.getCountry());
        binding.setStartedDate(tvShow.getStart_date());
        binding.textName.setVisibility(View.VISIBLE);
        binding.textNetworkCountry.setVisibility(View.VISIBLE);
        binding.textStartDate.setVisibility(View.VISIBLE);
        binding.textStatus.setVisibility(View.VISIBLE);


    }

}

