package org.ucomplex.ucomplex.Fragments;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.ucomplex.ucomplex.Activities.CourseActivity;
import org.ucomplex.ucomplex.Activities.Tasks.FetchUserEventsTask;
import org.ucomplex.ucomplex.Model.EventRowItem;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;

/**
 * @author Sermilion
 */
public class EventsFragment extends ListFragment {

    private ArrayList<EventRowItem> eventItems = null;
    ProgressDialog progressDialog;
	ImageAdapter imageAdapter;
    Button btnLoadExtra;
    ProgressDialog dialog;
    Context context;
    int userType;

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public EventsFragment(){

    }

    @Override
    public void onStart(){
        super.onStart();
        if (progressDialog!=null)
        progressDialog.dismiss();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);


        getListView().setAdapter(imageAdapter);
        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(eventItems.get(position).getType()!=2){

                }else {
                    progressDialog= ProgressDialog.show(getActivity(), "Загрузка...","BИдет загрузка", true);
                    progressDialog.getProgress();
                    Intent intent = new Intent(getActivity(), CourseActivity.class);
                    intent.putExtra("gcourse", eventItems.get(position).getParams().getGcourse());
                    intent.putExtra("type", eventItems.get(position).getType());
                    intent.putExtra("bitmap", eventItems.get(position).getEventImageBitmap());
                    startActivity(intent);
                }
            }
        });

        btnLoadExtra = new Button(getActivity());
        btnLoadExtra.setFocusable(false);
        btnLoadExtra.setText("Загрузить еще...");
        btnLoadExtra.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog = ProgressDialog.show(getActivity(), "",
                        "Идет подгрузка", true);
                dialog.show();
                new FetchUserEventsTask(getActivity()){
                    @Override
                    protected void onPostExecute(ArrayList<EventRowItem> items) {
                        super.onPostExecute(items);
                        if(items!=null){
                            eventItems.addAll(items);
                            imageAdapter.notifyDataSetChanged();

                        }else{
                            btnLoadExtra.setVisibility(View.GONE);
                        }
                        dialog.dismiss();
                        if(items.size()<10){
                            btnLoadExtra.setVisibility(View.GONE);
                        }
                    }
                }.execute(userType, eventItems.size());
            }
        });

        if(eventItems!=null){
            if (eventItems.size() < 10) {
                btnLoadExtra.setVisibility(View.GONE);
            }
            getListView().addFooterView(btnLoadExtra);
        }else{
            Toast.makeText(getActivity(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container, savedInstanceState);
        Bundle extras = getArguments();
        this.eventItems = (ArrayList<EventRowItem>) extras.getSerializable("eventItems");
		View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        imageAdapter = new ImageAdapter(getActivity());
		return rootView;
	}


    private class ImageAdapter extends BaseAdapter {


		private LayoutInflater inflater;
		private DisplayImageOptions options;
        protected ImageLoader imageLoader;
        Context context;

		ImageAdapter(Context context) {
            this.context = context;
			inflater = LayoutInflater.from(context);
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.mipmap.ic_ucomplex)
					.showImageForEmptyUri(R.mipmap.ic_ucomplex)
					.showImageOnFail(R.mipmap.ic_ucomplex)
					.cacheInMemory(true)
					.cacheOnDisk(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.build();
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		}

		@Override
		public int getCount() {
			if(eventItems!=null){
				return eventItems.size();
			}else{
				return 0;
			}

		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			View view = convertView;
            final ViewHolder viewHolder;
			if (view == null){

				view = inflater.inflate(R.layout.list_item_events, parent, false);
            viewHolder = new ViewHolder();
                assert view != null;
            viewHolder.eventsImageView = (ImageView) view.findViewById(R.id.list_events_item_image);
            viewHolder.eventTextView = (TextView) view.findViewById(R.id.list_events_item_text);
            viewHolder.eventTime = (TextView) view.findViewById(R.id.list_events_item_date);
            viewHolder.timeTextView = (TextView) view.findViewById(R.id.list_events_time_image);
				view.setTag(viewHolder);
			} else {
                viewHolder = (ViewHolder) view.getTag();
			}

			if(eventItems.get(position).getEventImageBitmap()==null && eventItems.get(position).getParams().getPhoto()==1) {
                imageLoader.displayImage("http://ucomplex.org/files/photos/" + eventItems.get(position).getParams().getCode() + ".jpg", viewHolder.eventsImageView, options, new SimpleImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri, View view) {
							}

							@Override
							public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

							}

							@Override
							public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								BitmapDrawable bitmapDrawable = ((BitmapDrawable) viewHolder.eventsImageView.getDrawable());
								Bitmap bitmap = bitmapDrawable.getBitmap();
								eventItems.get(position).setEventImageBitmap(bitmap);
								viewHolder.eventsImageView.setImageBitmap(bitmap);
							}
						}, new ImageLoadingProgressListener() {
							@Override
							public void onProgressUpdate(String imageUri, View view, int current, int total) {
							}
						});

			}else{
                Bitmap image = eventItems.get(position).getEventImageBitmap();
                if(image!=null)
                    viewHolder.eventsImageView.setImageBitmap(eventItems.get(position).getEventImageBitmap());
                else
                    viewHolder.eventsImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_ucomplex));
            }
			viewHolder.eventTextView.setText(eventItems.get(position).getEventText());
			viewHolder.eventTime.setText(eventItems.get(position).getTime());
            viewHolder.timeTextView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf"));
            viewHolder.timeTextView.setText("\uF017");

			return view;
		}
	}

	static class ViewHolder {
		ImageView eventsImageView;
		TextView eventTextView;
        TextView eventTime;
        TextView timeTextView;
	}


}

