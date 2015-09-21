package com.codepath.instagramphotoviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedDrawable;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> implements StickyListHeadersAdapter {

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final InstagramPhoto photo = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String photoCaption = photo.getCaption() == null ? "" : photo.getCaption();
        String usernamePlusCaption = photo.getUsername() + " " + photoCaption;

        Spannable photoCaptionSpan = new SpannableString(usernamePlusCaption);
        photoCaptionSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, photo.getUsername().length(), 0);
        photoCaptionSpan.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.blue_1)), 0,
                photo.getUsername().length(), 0);

        int end, start = usernamePlusCaption.indexOf("@");

        while (start >= 0) {

            end = usernamePlusCaption.substring(start).indexOf(" ") + start;
            if (end < 0 || end < start) end = usernamePlusCaption.length();
            photoCaptionSpan.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.blue_1)), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            start = usernamePlusCaption.indexOf("@", start + 1);
        }

        start = usernamePlusCaption.indexOf("#");

        while (start >= 0) {
            end = usernamePlusCaption.substring(start).indexOf(" ") + start;
            if (end < 0 || end < start) end = usernamePlusCaption.length();
            photoCaptionSpan.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.blue_1)), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            start = usernamePlusCaption.indexOf("#", start + 1);
        }

        viewHolder.tvCaption.setText(photoCaptionSpan);
        viewHolder.ivPhoto.setImageResource(0);
        String likes = getContext().getString(R.string.likes, photo.getLikesCount());
        viewHolder.tvLikes.setText(likes);

        Picasso.with(getContext())
                .load(photo.getImageUrl()).resize(0, photo.getImageHeight())
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.ivPhoto);

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);

        HeaderViewHolder headerViewHolder;
        if (convertView == null) {
            headerViewHolder = new HeaderViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.header_photo, parent, false);
            headerViewHolder.ivProfilePicture = (ImageView) convertView.findViewById(R.id.ivProfilePicture);
            headerViewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            headerViewHolder.tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
            headerViewHolder.tvCreatedTime = (TextView) convertView.findViewById(R.id.tvCreatedTime);
            convertView.setTag(headerViewHolder);
        } else {
            headerViewHolder = (HeaderViewHolder) convertView.getTag();
        }

        headerViewHolder.tvUsername.setText(photo.getUsername());
        headerViewHolder.ivProfilePicture.setImageResource(0);
        if (photo.getLocationName() != null) {
            headerViewHolder.tvLocation.setVisibility(View.VISIBLE);
            headerViewHolder.tvLocation.setText(photo.getLocationName());
        } else {
            headerViewHolder.tvLocation.setVisibility(View.GONE);
        }
        CharSequence createdTime = DateUtils.getRelativeTimeSpanString(photo.getCreatedTime(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
        headerViewHolder.tvCreatedTime.setText(createdTime);

        Picasso.with(getContext())
                .load(photo.getProfilePicture())
                .transform(mTransformation)
                .into(headerViewHolder.ivProfilePicture);

        return convertView;
    }

    @Override
    public long getHeaderId(int i) {
        return getItem(i).getId();
    }

    private static class ViewHolder {
        ImageView ivPhoto;
        TextView tvLikes;
        TextView tvCaption;
    }

    private static class HeaderViewHolder {
        ImageView ivProfilePicture;
        TextView tvUsername;
        TextView tvLocation;
        TextView tvCreatedTime;
    }

    private final Transformation mTransformation = new Transformation() {

        final float radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30,
                getContext().getResources().getDisplayMetrics());
        final int border = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f,
                getContext().getResources().getDisplayMetrics());
        final boolean oval = false;
        final int color = getContext().getResources().getColor(R.color.gray);

        @Override
        public Bitmap transform(Bitmap bitmap) {
            Bitmap transformed = RoundedDrawable.fromBitmap(bitmap)
                    .setBorderColor(color)
                    .setBorderWidth(border)
                    .setCornerRadius(radius)
                    .setOval(oval)
                    .toBitmap();
            if (!bitmap.equals(transformed)) {
                bitmap.recycle();
            }
            return transformed;
        }

        @Override
        public String key() {
            return "rounded_radius_" + radius + "_border_" + border + "_color_" + color + "_oval_" + oval;
        }
    };
}
