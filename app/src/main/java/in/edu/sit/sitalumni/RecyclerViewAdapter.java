package in.edu.sit.sitalumni;

import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Newsfeed> feedList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, date, message;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            date = (TextView) view.findViewById(R.id.date);
            message = (TextView) view.findViewById(R.id.message);
        }
    }


    public RecyclerViewAdapter(List<Newsfeed> feedList) {
        this.feedList = feedList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Newsfeed movie = feedList.get(position);
        holder.name.setText(movie.getName());
        holder.date.setText(movie.getDate());
        holder.message.setText(movie.getMessage());
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }
}