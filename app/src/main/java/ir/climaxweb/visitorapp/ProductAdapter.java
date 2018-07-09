package ir.climaxweb.visitorapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder>{

    private List<Product> productList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView productId, productName, productInStock, productPrice, productSalePrice, productDescription;
        public ImageView productPicture;

        public MyViewHolder(View view) {
            super(view);
            productId = (TextView) view.findViewById(R.id.productId);
            productName = (TextView) view.findViewById(R.id.productName);
            productInStock = (TextView) view.findViewById(R.id.productInStock);
            productPrice = (TextView) view.findViewById(R.id.productPrice);
            productSalePrice = (TextView) view.findViewById(R.id.productSalePrice);
            productDescription = (TextView) view.findViewById(R.id.productDescription);
            productPicture = (ImageView) view.findViewById(R.id.productPicture);
        }
    }


    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_row, parent, false);

        return new ProductAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.MyViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productId.setText("ID: "+Integer.toString(product.getId()));
        holder.productName.setText("Name: "+product.getName());
        if(product.isIn_stock()){
            holder.productInStock.setText("In stock: Yes");
        }
        else{
            holder.productInStock.setText("In stock: No!");
        }
        holder.productPrice.setText("Price: "+product.getPrice());
        holder.productSalePrice.setText("Sale price: "+product.getSale_price());
        holder.productDescription.setText("Price: "+product.getDescription());

        String base64String = product.getPicture();
        String base64Image = base64String.split(",")[1];

        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        holder.productPicture.setImageBitmap(decodedByte);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
