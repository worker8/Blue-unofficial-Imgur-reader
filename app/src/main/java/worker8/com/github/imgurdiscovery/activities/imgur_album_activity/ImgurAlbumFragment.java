package worker8.com.github.imgurdiscovery.activities.imgur_album_activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import worker8.com.github.imgurdiscovery.R;
import worker8.com.github.imgurdiscovery.util.ActionUtil;
import worker8.com.github.jimgur.imgur.album_api.AlbumResponse;
import worker8.com.github.jimgur.imgur.album_api.Images;
import worker8.com.github.jimgur.imgur.gallery_api.GalleryResponse;


public class ImgurAlbumFragment extends android.support.v4.app.Fragment {
    int position;
    @Bind(R.id.fake_status_bar)
    View fakeStatusBar;

    @Bind(R.id.imgur_album_image)
    PhotoView imageView;
    @Bind(R.id.imgur_album_image_title)
    TextView imgurAlbumImageTitle;
    @Bind(R.id.imgur_album_sequence_tag)
    TextView imgurAlbumSequenceTag;
    @Bind(R.id.imgur_album_grid)
    ImageView imgurAlbumGrid;
    @Bind(R.id.imgur_album_back_arrow)
    ImageView imgurAlbumBackArrow;
    @Bind(R.id.imgur_album_progress_bar)
    ProgressBar imgurAlbumProgressBar;
    @Bind(R.id.imgur_album_open_in_browser)
    ImageView imgurAlbumOpenInBrowser;
    @Bind(R.id.imgur_album_copy_link)
    ImageView imgurAlbumCopyLink;
    @Bind(R.id.imgur_album_share)
    ImageView imgurAlbumShare;
    @Bind(R.id.imgur_album_download)
    ImageView imgurAlbumDownload;
    @Bind(R.id.imgur_album_toolbar)
    Toolbar imgurAlbumToolbar;

    ImgurAlbumActivity activity;
    Images[] images;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_POSITION = "ARG_SECTION_POSITION";

    /**
     * Returns a new instance of this fragment for the given section number.
     */

    public static ImgurAlbumFragment newInstance(int sectionNumber) {
        ImgurAlbumFragment fragment = new ImgurAlbumFragment();
        Bundle args = new Bundle();
        args.putInt(ImgurAlbumFragment.ARG_SECTION_POSITION, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ImgurAlbumFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_SECTION_POSITION);
        activity = (ImgurAlbumActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (activity.albumData instanceof AlbumResponse) {
            images = ((AlbumResponse) activity.albumData).getData().getImages();
        } else {
            images = ((GalleryResponse) activity.albumData).getData().getImages();
        }

        View rootView = inflater.inflate(R.layout.fragment_imgur_album, container, false);
        ButterKnife.bind(this, rootView);

        loadImage();
        setupTitle();
        setupActionButtons();

        imgurAlbumSequenceTag.setText((position + 1) + " of " + images.length);
        imgurAlbumGrid.setOnClickListener(activity.onClickListenerGridIcon);

        return rootView;
    }

    public void loadImage() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imgurAlbumProgressBar.setVisibility(View.VISIBLE);
        imageLoader.loadImage(images[position].getLink(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                if (activity != null) { // do not continue code

                    imageView.setImageBitmap(bitmap);
//                    AnimUtil.fadeIn(activity, imageView);
                    // progress bar
                    imgurAlbumProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void setupTitle() {
        if (images[position].getTitle() != null && images[position].getDescription() != null) {
            imgurAlbumImageTitle.setText(images[position].getTitle() + " - " + images[position].getDescription());
        } else if (images[position].getTitle() == null && images[position].getDescription() == null) {
            imgurAlbumImageTitle.setText("Gallery");
        } else if (images[position].getTitle() == null) {
            imgurAlbumImageTitle.setText(images[position].getDescription());
        } else if (images[position].getDescription() == null) {
            imgurAlbumImageTitle.setText(images[position].getTitle());
        }
        imgurAlbumImageTitle.setOnClickListener(view -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);

            View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_show_full_title, null);

            TextView dialogTitle = (TextView) dialogView.findViewById(R.id.title_dialog);
            if (images[position].getTitle() != null) {
                dialogTitle.setText(images[position].getTitle());
            } else {
                dialogTitle.setText("(no title)");
            }
            TextView dialogDescription = (TextView) dialogView.findViewById(R.id.dialog_description);
            if (images[position].getDescription() != null) {
                dialogDescription.setText(images[position].getDescription());
            } else {
                dialogDescription.setText("(no description)");
            }
            alert.setView(dialogView);
            alert.show();
        });
    }

    public void setupActionButtons() {
        imgurAlbumBackArrow.setOnClickListener(activity.onClickListenerBackIcon);
            /* open in browser */
        imgurAlbumOpenInBrowser.setOnClickListener(view -> {
            ActionUtil.openInBrowser(activity, images[position].getLink());
        });
        imgurAlbumShare.setOnClickListener(view -> {
            ActionUtil.shareUrl(activity, images[position].getLink());
        });

        imgurAlbumCopyLink.setOnClickListener(view -> {
            ActionUtil.copyText(activity, images[position].getLink());
            Toast.makeText(activity, "Link Copied!", Toast.LENGTH_SHORT).show();
        });

        imgurAlbumDownload.setOnClickListener(view -> {
            ActionUtil.downloadImage(activity, images[position].getLink());
        });
    }

}


