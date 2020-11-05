package activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.app.melhorevento.R;
import mehdi.sakout.aboutpage.AboutPage;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String descricao = "O aplicativo Melhor Evento foi desenvolvido pela Betabyte Soluções Tecnológicas com o objetivo de integrar a comunidade e os meios comerciais, a fim de " +
                "divulgar eventos, descontos e promoções de diferentes tipos de comercio. Tudo isso é feito de forma simples, por meio de " +
                "buscas por cidades específicas e pelo tipo de comércio.\n\nA betabyte soluções tecnológicas é uma empresa que visa a criação de " +
                "aplicações simples e práticas, que possam contribuir com a evolução da comunidade. Estamos sempre estudando novas " +
                "tecnologias e meios para inovar em nossos trabalhos.\n\nCaso tenha interesse em entrar em contato com a empresa, seja " +
                "com dúvidas, elogios, críticas ou para reportar defeitos, favor enviar e-mail utilizando o botão abaixo. Se possível, " +
                "siga nossa empresa nas redes socias. Obrigado por utilizar nosso produto.";
        View v = new AboutPage(this)
                .setImage(R.drawable.logo_app)
                .setDescription(descricao)
                .addGroup("Fale conosco")
                .addEmail("betabytesolucoestecnologicas@gmail.com","E-mail")
                .addGroup("Redes sociais")
                .addFacebook("betabytesolucoestecnologicas","Facebook")
                .create();
        setContentView(v);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}