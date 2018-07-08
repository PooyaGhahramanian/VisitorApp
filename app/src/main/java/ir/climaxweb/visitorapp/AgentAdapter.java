package ir.climaxweb.visitorapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.MyViewHolder> {

    private List<Agent> agentList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView agentId, agentName, agentAddress;

        public MyViewHolder(View view) {
            super(view);
            agentId = (TextView) view.findViewById(R.id.agentId);
            agentName = (TextView) view.findViewById(R.id.agentName);
            agentAddress = (TextView) view.findViewById(R.id.agentAddress);
        }
    }


    public AgentAdapter(List<Agent> agentList) {
        this.agentList = agentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.agent_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Agent agent = agentList.get(position);
        holder.agentId.setText("ID: "+Integer.toString(agent.getId()));
        holder.agentName.setText("Name: "+agent.getName());
        holder.agentAddress.setText("Address: "+agent.getAddress());
    }

    @Override
    public int getItemCount() {
        return agentList.size();
    }
}