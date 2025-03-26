package com.example.gradecalculatorapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ModulesAdapter extends RecyclerView.Adapter<ModulesAdapter.ViewHolder> {
    private List<Modules> modulesList;

    public ModulesAdapter(List<Modules> modulesList) {
        this.modulesList = modulesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_module, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Modules module = modulesList.get(position);
        holder.titleTextView.setText(module.title);
        holder.descriptionTextView.setText(module.description);
        holder.gradeTextView.setText("Target: " + module.targetGrade + " | Current: " + module.currentGrade);
    }

    @Override
    public int getItemCount() {
        return modulesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, gradeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.moduleTitle);
            descriptionTextView = itemView.findViewById(R.id.moduleDescription);
            gradeTextView = itemView.findViewById(R.id.moduleGrades);
        }
    }
}
