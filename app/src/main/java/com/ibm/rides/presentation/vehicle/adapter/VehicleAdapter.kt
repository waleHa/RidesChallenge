import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ibm.domain.model.VehicleResponse
import com.ibm.rides.databinding.ItemVehicleBinding

class VehicleAdapter(private val onClick: (VehicleResponse) -> Unit) :
    ListAdapter<VehicleResponse, VehicleAdapter.VehicleViewHolder>(VehicleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val binding = ItemVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehicleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = getItem(position)
        holder.bind(vehicle)
    }

    inner class VehicleViewHolder(private val binding: ItemVehicleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vehicle: VehicleResponse) {
            binding.vehicleMakeModelTextView.text = vehicle.makeAndModel
            binding.vehicleVinTextView.text = vehicle.vin
            binding.root.setOnClickListener {
                onClick(vehicle)
            }
        }
    }

    class VehicleDiffCallback : DiffUtil.ItemCallback<VehicleResponse>() {
        override fun areItemsTheSame(oldItem: VehicleResponse, newItem: VehicleResponse): Boolean {
            return oldItem.vin == newItem.vin
        }

        override fun areContentsTheSame(oldItem: VehicleResponse, newItem: VehicleResponse): Boolean {
            return oldItem == newItem
        }
    }
}
