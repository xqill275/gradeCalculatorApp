package com.example.gradecalculatorapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ModulesAdapter extends RecyclerView.Adapter<ModulesAdapter.ViewHolder> {
    private Context context;
    private List<Modules> modulesList;
    private ModulesDao modulesDao;

    public ModulesAdapter(Context context, List<Modules> modulesList, ModulesDao modulesDao) {
        this.context = context;
        this.modulesList = modulesList;
        this.modulesDao = modulesDao;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_module, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Modules module = modulesList.get(position);
        holder.titleTextView.setText(module.title);
        holder.descriptionTextView.setText(module.description);
        holder.gradeTextView.setText("Target: " + module.targetGrade + " | Current: " + module.currentGrade);

        // Handle DELETE button
        holder.deleteButton.setOnClickListener(v -> {
            modulesDao.deleteModule(module.moduleID);
            modulesList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, modulesList.size());
            Toast.makeText(context, "Module Deleted", Toast.LENGTH_SHORT).show();
        });

        // Handle EDIT button (Open EditModulesActivity)
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditModulesActivity.class);
            intent.putExtra("moduleID", module.moduleID);  // Pass module ID
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return modulesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, gradeTextView;
        Button editButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.moduleTitle);
            descriptionTextView = itemView.findViewById(R.id.moduleDescription);
            gradeTextView = itemView.findViewById(R.id.moduleGrades);
            editButton = itemView.findViewById(R.id.editModuleButton);
            deleteButton = itemView.findViewById(R.id.deleteModuleButton);
        }
    }

    // Method to update the list
    public void updateModules(List<Modules> newModules) {
        modulesList.clear();
        modulesList.addAll(newModules);
        notifyDataSetChanged();
    }
}
