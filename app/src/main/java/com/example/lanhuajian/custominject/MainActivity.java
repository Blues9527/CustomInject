package com.example.lanhuajian.custominject;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lanhuajian.custominject.inject.OnClick;
import com.example.lanhuajian.custominject.inject.SetContentView;
import com.example.lanhuajian.custominject.inject.ViewBinding;

@SetContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewBinding(R.id.btn)
    private Button btn;
    @ViewBinding(R.id.tv)
    private TextView tv;


    @OnClick({R.id.btn, R.id.tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                Toast.makeText(this, btn.getText().toString(), Toast.LENGTH_SHORT).show();
                break;

            case R.id.tv:
                Toast.makeText(this, tv.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
