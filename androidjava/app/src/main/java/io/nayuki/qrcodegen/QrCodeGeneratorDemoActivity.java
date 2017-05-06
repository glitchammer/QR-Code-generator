package io.nayuki.qrcodegen;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class QrCodeGeneratorDemoActivity extends AppCompatActivity {


    public interface Demo {
        public Bitmap createQrBitmap();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_generator_demo);

        final ArrayList<Demo> demos = new ArrayList<Demo>();


        // basic demo
        demos.add(new Demo() {
            @Override
            public Bitmap createQrBitmap() {
                String text = "Hello, world!";          // User-supplied Unicode text
                QrCode.Ecc errCorLvl = QrCode.Ecc.LOW;  // Error correction level

                QrCode qr = QrCode.encodeText(text, errCorLvl);  // Make the QR Code symbol
                Bitmap img = qr.toImage(10, 4);          // Convert to bitmap image

                return img;
            }
        });

        // Project Nayuki URL
        demos.add(new Demo() {
            @Override
            public Bitmap createQrBitmap() {
                QrCode qr = QrCode.encodeText("https://www.nayuki.io/", QrCode.Ecc.HIGH);
                qr = new QrCode(qr, 3);  // Change mask, forcing to mask #3
                return qr.toImage(8, 6);
            }
        });

        // Numeric mode encoding (3.33 bits per digit)
        demos.add(new Demo() {
            @Override
            public Bitmap createQrBitmap() {
                QrCode qr = QrCode.encodeText("314159265358979323846264338327950288419716939937510", QrCode.Ecc.MEDIUM);
                return qr.toImage(13, 1);
            }
        });

        // Alphanumeric mode encoding (5.5 bits per character)
        demos.add(new Demo() {
            @Override
            public Bitmap createQrBitmap() {
                QrCode qr = QrCode.encodeText("DOLLAR-AMOUNT:$39.87 PERCENTAGE:100.00% OPERATIONS:+-*/", QrCode.Ecc.HIGH);
                return qr.toImage(10, 2);
            }
        });

        //todo glitchammer: at this point I realized, for a proper demo, there must be headings for each demo (just like the filenames in the original module by nayuki)
        //
        // Unicode text as UTF-8, and different masks
        // a series of 4
        final String txtNoIdeaWhatItMeans = "こんにちwa、世界！ αβγδ";
        demos.add(new Demo() {
            @Override
            public Bitmap createQrBitmap() {
                QrCode qr = QrCode.encodeText(txtNoIdeaWhatItMeans, QrCode.Ecc.QUARTILE);
                return new QrCode(qr, 0).toImage(10, 3);
            }
        });
        demos.add(new Demo() {
            @Override
            public Bitmap createQrBitmap() {
                QrCode qr = QrCode.encodeText(txtNoIdeaWhatItMeans, QrCode.Ecc.QUARTILE);
                return new QrCode(qr, 1).toImage(10, 3);
            }
        });
        demos.add(new Demo() {
            @Override
            public Bitmap createQrBitmap() {
                QrCode qr = QrCode.encodeText(txtNoIdeaWhatItMeans, QrCode.Ecc.QUARTILE);
                return new QrCode(qr, 5).toImage(10, 3);
            }
        });
        demos.add(new Demo() {
            @Override
            public Bitmap createQrBitmap() {
                QrCode qr = QrCode.encodeText(txtNoIdeaWhatItMeans, QrCode.Ecc.QUARTILE);
                return new QrCode(qr, 7).toImage(10, 3);
            }
        });

        // Moderately large QR Code using longer text (from Lewis Carroll's Alice in Wonderland)
        demos.add(new Demo() {
            @Override
            public Bitmap createQrBitmap() {
                QrCode qr = QrCode.encodeText(
                        "Alice was beginning to get very tired of sitting by her sister on the bank, "
                                + "and of having nothing to do: once or twice she had peeped into the book her sister was reading, "
                                + "but it had no pictures or conversations in it, 'and what is the use of a book,' thought Alice "
                                + "'without pictures or conversations?' So she was considering in her own mind (as well as she could, "
                                + "for the hot day made her feel very sleepy and stupid), whether the pleasure of making a "
                                + "daisy-chain would be worth the trouble of getting up and picking the daisies, when suddenly "
                                + "a White Rabbit with pink eyes ran close by her.", QrCode.Ecc.HIGH);
                return qr.toImage(6, 10);
            }
        });


        // add it to the grid
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return demos.size();
            }

            @Override
            public Object getItem(int position) {
                return demos.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageView;
                if (convertView == null) {
                    // if it's not recycled, initialize some attributes
                    imageView = new ImageView(QrCodeGeneratorDemoActivity.this);
                    imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT, GridView.LayoutParams.WRAP_CONTENT));
                    imageView.setScaleType(ImageView.ScaleType.CENTER);
                    imageView.setPadding(8, 8, 8, 8);
                } else {
                    imageView = (ImageView) convertView;
                }
                Demo d = demos.get(position);
                Bitmap bitmap = d.createQrBitmap();
                imageView.setImageBitmap(bitmap);
                return imageView;
            }
        });

    }
}
