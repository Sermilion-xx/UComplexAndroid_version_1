package org.ucomplex.ucomplex.Activities;

import android.os.Bundle;


import org.ucomplex.ucomplex.R;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;


public class WebViewActivity extends AppCompatActivity {

    private WebView webView;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.web_toolbar);
        toolbar.setTitle("Анкетирование");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            webView.loadDataWithBaseURL("", a+b+c+d+e+f, "text/html", "UTF-8", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    String a = "<div class=\"teacher-votes-wrap dpl dpr\">\n" +
            "\t\t<div class=\"teacher-voting\">\n" +
            "\t\t\t\t\t\t\t<div class=\"part\">Раздел №1. Учебная работа</div>\n" +
            "\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title\">Оцените качество проведения практик (ознакомительных, дипломных и т.д.) в Вашей образовательной организации</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[1]\"><span class=\"middle\">Практики, в которых я принимал участие, были абсолютно бесполезны для меня и для результатов обучения в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[1]\"><span class=\"middle\">Практики, в которых я принимал участие, принесли мне незначительную пользу</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"3\" data-send=\"\" type=\"radio\" name=\"qs[1]\"><span class=\"middle\">Практики, в которых я принимал участие, были в целом полезны для меня и для результатов обучения в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"4\" data-send=\"\" type=\"radio\" name=\"qs[1]\"><span class=\"middle\">Практики, в которых я принимал участие, были полезны для меня и для результатов обучения в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"5\" data-send=\"\" type=\"radio\" name=\"qs[1]\"><span class=\"middle\">Практики, в которых я принимал участие, были исключительно полезны для меня и для результатов обучения в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title group\">Оцените качество организации практик (ознакомительных, дипломных и т.д.) в Вашей образовательной организации (заполняется в том случае, если Вам уже приходилось проходить любую форму практики):</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Отдел практики моей образовательной организации помог мне найти место прохождения практики</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[2][1]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[2][1]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Отдел практики оказывал мне помощь в оформлении документов по итогам практики</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[2][2]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[2][2]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Я проходил практику в организации, деятельность которой совпадала с моей специальностью</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[2][3]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[2][3]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Прохождение практики дало мне настоящие навыки и практический опыт</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[2][4]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[2][4]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Планирую пройти практику в той же организации, поскольку мной заинтересовались как потенциальным сотрудником</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[2][5]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[2][5]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title group\">Оцените использование преподавателями современных образовательных технологий:</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Преподаватели используют при проведении занятий мультимедийное оборудование</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[3][1]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[3][1]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Преподаватели используют при проведении занятий раздаточный материал (рабочие тетради и т.д.)</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[3][2]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[3][2]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Преподаватели используют при проведении занятий интерактивные формы работы (деловые игры, решение ситуационных задач и т.д.)</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[3][3]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[3][3]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Преподаватели советуют необходимую литературу, подсказывают информационные ресурсы для получения необходимой информации</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[3][4]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[3][4]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Преподаватели применяют технологии сбора обратной связи, используют отзывы обучающихся для совершенствования своей техники преподавания</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[3][5]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[3][5]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title group\">Оцените степень доступности изложения материала и взаимодействия преподавателя с обучающимися:</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Преподаватели оказывают консультационную помощь студентам во внеучебное время</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[4][1]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[4][1]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Преподаватели дают практические примеры к преподаваемому теоретическому материалу</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[4][2]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[4][2]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Преподаватели дают материал в форме, которая хорошо и полностью усваивается</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[4][3]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[4][3]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Я успеваю фиксировать всю необходимую мне информацию на лекционных занятиях</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[4][4]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[4][4]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Практические занятия дают конкретные умения и навыки, которые я должен обрести по итогам освоения дисциплины</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[4][5]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[4][5]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title group\">Оцените профессионализм профессорско-преподавательского состава Вашей образовательной организации:</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Преподаватели владеют своим предметом, знают его специфику и особенности</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[5][1]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[5][1]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Преподаватели имеют профессиональный внешний вид, одеваются соответствующим образом</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[5][2]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[5][2]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Преподаватели вежливы и корректны с обучающимися</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[5][3]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[5][3]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Преподаватели не опаздывают на занятия</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[5][4]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[5][4]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Преподаватели имеют грамотно поставленную речь</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[5][5]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[5][5]\"><span class=\"middle\">Нет</span></label></div>";

    String b = "<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title\">Оцените, насколько вы уверены в актуальности получаемых в ходе обучения в образовательной организации знаний</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[6]\"><span class=\"middle\">Знания, которые я получаю, неактуальны. Я не уверен, что они будут полезны в моей дальнейшей профессиональной деятельности</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[6]\"><span class=\"middle\">Знания, которые я получаю, имеют незначительную степень актуальности</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"3\" data-send=\"\" type=\"radio\" name=\"qs[6]\"><span class=\"middle\">Знания, которые я получаю, в целом актуальности</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"4\" data-send=\"\" type=\"radio\" name=\"qs[6]\"><span class=\"middle\">Знания, которые я получаю, по большей части актуальны</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"5\" data-send=\"\" type=\"radio\" name=\"qs[6]\"><span class=\"middle\">Знания, которые я получаю, актуальны. Я полностью уверен, что они будут полезны в моей дальнейшей профессиональной деятельности</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title\">Оцените объективность контроля качества Ваших знаний, получаемых в ходе обучения в Вашей образовательной организации</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[7]\"><span class=\"middle\">Знания оцениваются необъективно, оценка зависит от субъективных факторов и личного отношения преподавателя ко мне</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[7]\"><span class=\"middle\">Знания оцениваются по большей части необъективно за исключением ряда преподавателей</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"3\" data-send=\"\" type=\"radio\" name=\"qs[7]\"><span class=\"middle\">Знания оцениваются в целом объективно</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"4\" data-send=\"\" type=\"radio\" name=\"qs[7]\"><span class=\"middle\">Знания оцениваются по большей мере объективно за исключением ряда преподавателей</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"5\" data-send=\"\" type=\"radio\" name=\"qs[7]\"><span class=\"middle\">Знания оцениваются полностью объективно и на основании моих реальных знаний</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title group\">Оцените, насколько образовательная организация высшего образования обеспечивает Ваш доступ к необходимым для обучения материалам:</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">В библиотеке образовательной организации всегда можно найти необходимый для обучения материал</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[8][1]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[8][1]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">В библиотеке образовательной организации имеются актуальные научные периодические издания (журналы, бюллетени и т.д.)</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[8][2]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[8][2]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">В образовательной организации есть доступ к электронным библиотекам и базам данных</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[8][3]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[8][3]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Образовательная организация обеспечивает возможность работать с иностранными источниками информации</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[8][4]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[8][4]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Образовательная организация обеспечивает достаточную по времени возможность использования коллективных пунктов доступа к Интернету</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[8][5]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[8][5]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title\">Оцените свою осведомленность об образовательной программе, по которой Вы обучаетесь (перечень и структура дисциплин, учебный план и т.д.)</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[9]\"><span class=\"middle\">Я не интересуюсь составом своей образовательной программы</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[9]\"><span class=\"middle\">Я имею общее представление о своей образовательной программе и мне не нужна дополнительная информация</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"3\" data-send=\"\" type=\"radio\" name=\"qs[9]\"><span class=\"middle\">Я в целом осведомлен о своей образовательной программе</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"4\" data-send=\"\" type=\"radio\" name=\"qs[9]\"><span class=\"middle\">Я хорошо осведомлен о своей образовательной программе, но не знаю, где можно получить дополнительную информацию</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"5\" data-send=\"\" type=\"radio\" name=\"qs[9]\"><span class=\"middle\">Я хорошо осведомлен о своей образовательной программе и знаю, где можно получить дополнительную информацию</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title group\">Оцените свою возможность влиять на структуру и качество образовательной программы:</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">У меня есть возможность выбирать предметы (часть предметов), по которым я хотел бы обучаться</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[10][1]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[10][1]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Образовательная организация обеспечивает возможность получения дополнительных знаний по тем предметам, которые меня интересуют</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[10][2]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[10][2]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Я принимал участие в мониторинге качества образовательных услуг и проходил соответствующее анкетирование</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[10][3]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[10][3]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Я принимал участие в составлении рейтинга преподавателей</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[10][4]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[10][4]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">В образовательной организации есть специалисты, к которым можно обратиться за консультацией при формировании своей индивидуальной образовательной траектории</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[10][5]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[10][5]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title\">Оцените созданные в образовательной организации условия для занятий научно-инновационной деятельностью</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[11]\"><span class=\"middle\">Я не занимаюсь научно-инновационной деятельностью</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[11]\"><span class=\"middle\">Моя научно-инновационная деятельность ограничивается участием в студенческих научно-практических конференциях</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"3\" data-send=\"\" type=\"radio\" name=\"qs[11]\"><span class=\"middle\">В образовательной организации достаточные условия для занятий научно-инновационной деятельностью</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"4\" data-send=\"\" type=\"radio\" name=\"qs[11]\"><span class=\"middle\">В образовательной организации созданы хорошие условия для занятий научно-инновационной деятельностью</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"5\" data-send=\"\" type=\"radio\" name=\"qs[11]\"><span class=\"middle\">В образовательной организации созданы все условия для занятий научно-инновационной деятельностью, включая возможность коммерциализировать свои разработки</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<div class=\"part\">Раздел № 2. Внеучебная работа</div>\n" +
            "\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title\">Оцените количество мероприятий по воспитательной работе, которые проводятся в Вашей образовательной организации</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[12]\"><span class=\"middle\">Я совершенно не интересуюсь тем, какая воспитательная работа проводится в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[12]\"><span class=\"middle\">Мероприятия в моей образовательной организации практически не проводятся</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"3\" data-send=\"\" type=\"radio\" name=\"qs[12]\"><span class=\"middle\">Проводится не так много мероприятий, но для меня этот выбор достаточен</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"4\" data-send=\"\" type=\"radio\" name=\"qs[12]\"><span class=\"middle\">Проводится достаточное число мероприятий. Я всегда могу выбрать то, что мне интересно</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"5\" data-send=\"\" type=\"radio\" name=\"qs[12]\"><span class=\"middle\">Проводится большое число мероприятий. Мне сложно выбрать то, которое мне наиболее интересно</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>";

    String c = "<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title\">Оцените качество мероприятий по воспитательной работе, которые проводятся в Вашей образовательной организации</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[13]\"><span class=\"middle\">Я совершенно не интересуюсь тем, какая воспитательная работа проводится в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[13]\"><span class=\"middle\">Мероприятия в моей образовательной организации проводятся на низком уровне, мне не хочется их посещать</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"3\" data-send=\"\" type=\"radio\" name=\"qs[13]\"><span class=\"middle\">Проводится достаточно мероприятий, но среди них не так много качественных и интересных для меня</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"4\" data-send=\"\" type=\"radio\" name=\"qs[13]\"><span class=\"middle\">Проводятся качественные и интересные мероприятия. Я всегда могу выбрать то, что мне интересно</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"5\" data-send=\"\" type=\"radio\" name=\"qs[13]\"><span class=\"middle\">Проводятся качественные и интересные мероприятия. Мне сложно выбрать то, которое мне наиболее интересно</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title\">Оцените разнообразие мероприятий по воспитательной работе, которые проводятся в Вашей образовательной организации</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[14]\"><span class=\"middle\">Я совершенно не интересуюсь тем, какая воспитательная работа проводится в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[14]\"><span class=\"middle\">Мероприятия в моей образовательной организации практически не проводятся. Сложно говорить о разнообразии</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"3\" data-send=\"\" type=\"radio\" name=\"qs[14]\"><span class=\"middle\">Проводится достаточно мероприятий, но они все в целом однотипны</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"4\" data-send=\"\" type=\"radio\" name=\"qs[14]\"><span class=\"middle\">Проводятся разнообразные мероприятия. Я могу выбрать то, что мне интересно</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"5\" data-send=\"\" type=\"radio\" name=\"qs[14]\"><span class=\"middle\">Проводится много разнообразных мероприятий. Мне сложно выбрать то, которое мне наиболее интересно</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title\">Оцените, как часто вы принимаете участие в мероприятиях по воспитательной работе, проводимых в Вашей образовательной организации</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[15]\"><span class=\"middle\">Я совершенно не участвую в мероприятиях по воспитательной работе, проводимых в нашей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[15]\"><span class=\"middle\">Я иногда участвую в мероприятиях по воспитательной работе, проводимых в нашей образовательной организации, но делаю это в основном по указанию администрации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"3\" data-send=\"\" type=\"radio\" name=\"qs[15]\"><span class=\"middle\">Я периодически участвую в мероприятиях по воспитательной работе, проводимых в нашей образовательной организации, и делаю это по своей воле</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"4\" data-send=\"\" type=\"radio\" name=\"qs[15]\"><span class=\"middle\">Я стараюсь участвовать в большинстве проводимых мероприятий по воспитательной работе в своей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"5\" data-send=\"\" type=\"radio\" name=\"qs[15]\"><span class=\"middle\">Я с удовольствием участвую во всех мероприятиях, проводимых в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title\">Оцените, насколько хорошо Вы информированы о реализуемой в Вашей образовательной организации воспитательной (внеучебной) работе</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[16]\"><span class=\"middle\">Я совершенно не интересуюсь тем, какая воспитательная работа проводится в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[16]\"><span class=\"middle\">Я совершенно не знаю о том, какая воспитательная работа проводится в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"3\" data-send=\"\" type=\"radio\" name=\"qs[16]\"><span class=\"middle\">Я в целом имею представление о том, какая воспитательная работа реализуется в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"4\" data-send=\"\" type=\"radio\" name=\"qs[16]\"><span class=\"middle\">Я достаточно информирован о том, какая воспитательная работа реализуется в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"5\" data-send=\"\" type=\"radio\" name=\"qs[16]\"><span class=\"middle\">Я полностью информирован о том, какая воспитательная работа реализуется в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title\">Оцените, насколько доступна информация о проводимой воспитательной работе в Вашей образовательной организации</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[17]\"><span class=\"middle\">Я совершенно не интересуюсь информацией о воспитательной работе в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[17]\"><span class=\"middle\">Информация в плохом доступе, я не знаю, где можно получить необходимую информацию в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"3\" data-send=\"\" type=\"radio\" name=\"qs[17]\"><span class=\"middle\">Информация в среднем доступе. Я в целом имею представление, где можно получить необходимую информацию в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"4\" data-send=\"\" type=\"radio\" name=\"qs[17]\"><span class=\"middle\">Информация в хорошем доступе. Я знаю, где можно получить необходимую информацию в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"5\" data-send=\"\" type=\"radio\" name=\"qs[17]\"><span class=\"middle\">Информация в полном доступе. Я всегда знаю, где и у кого можно получить необходимую информацию в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title\">Оцените, насколько Вы осведомлены о деятельности совета обучающихся в Вашей образовательной организации</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[18]\"><span class=\"middle\">Я не знаю о существовании совета обучающихся в моей образовательной организации</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[18]\"><span class=\"middle\">Я что-то слышал о совете обучающихся в моей образовательной организации, но никогда не сталкивался с его работой</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"3\" data-send=\"\" type=\"radio\" name=\"qs[18]\"><span class=\"middle\">Я знаю, что в моей образовательной организации есть совет обучающихся, и проинформирован о его деятельности, но никогда не сталкивался с его работой</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"4\" data-send=\"\" type=\"radio\" name=\"qs[18]\"><span class=\"middle\">Я имею достаточно информации о совете обучающихся в моей образовательной организации, мне приходилось сталкиваться с его работой</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"5\" data-send=\"\" type=\"radio\" name=\"qs[18]\"><span class=\"middle\">Я имею полную информацию о совете обучающихся, активно участвую в его мероприятиях или в его работе</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<div class=\"part\">Раздел № 3. Инфраструктура</div>\n" +
            "\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title\">Оцените комфортность (температура, освещенность, состояние мебели и т.д.) аудиторий для проведения занятий в Вашей образовательной организации</div>";

    String d = "<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[19]\"><span class=\"middle\">Я полностью не удовлетворён качеством аудиторий, подавляющая часть аудиторий не приспособлена для обучения</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[19]\"><span class=\"middle\">Я частично удовлетворён качеством аудиторий, но очень большая часть аудиторий не приспособлена для обучения</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"3\" data-send=\"\" type=\"radio\" name=\"qs[19]\"><span class=\"middle\">В целом удовлетворен качеством аудиторий. Большинство аудиторий приспособлено для обучения</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"4\" data-send=\"\" type=\"radio\" name=\"qs[19]\"><span class=\"middle\">Я удовлетворен качеством аудиторий. Почти все аудитории, за небольшими исключениями, приспособлены для обучения</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"5\" data-send=\"\" type=\"radio\" name=\"qs[19]\"><span class=\"middle\">Я полностью удовлетворен. Все аудитории приспособлены для обучения</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title\">Оцените оснащенность (мультимедийное оборудование, лабораторное оборудование и т.д.) аудиторий/лабораторий для проведения занятий в Вашей образовательной организации</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[20]\"><span class=\"middle\">Я полностью не удовлетворён оснащением аудиторий/лабораторий, нужного оборудования почти нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[20]\"><span class=\"middle\">Я полностью удовлетворён оснащением аудиторий/лабораторий, но очень большая часть оборудования устарела или нуждается в замене</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"3\" data-send=\"\" type=\"radio\" name=\"qs[20]\"><span class=\"middle\">Я в целом удовлетворён оснащением аудиторий/лабораторий, большинство аудиторий/лабораторий оснащено на нужном уровне</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"4\" data-send=\"\" type=\"radio\" name=\"qs[20]\"><span class=\"middle\">Я удовлетворён оснащением аудиторий/лабораторий, почти все аудитории/лаборатории оснащены на нужном уровне</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"5\" data-send=\"\" type=\"radio\" name=\"qs[20]\"><span class=\"middle\">Я удовлетворён оснащением аудиторий/лабораторий, все аудитории/лаборатории оснащены на нужном уровне</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title group\">Оцените доступность и качество организации питания в Вашей образовательной организации:</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">В моей образовательной организации есть буфеты, где можно перекусить</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[21][1]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[21][1]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Качество (свежесть) предлагаемого питания меня удовлетворяет</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[21][2]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[21][2]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">В моей образовательной организации можно приобрести комплексный обед</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[21][3]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[21][3]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Предлагаемый ассортимент блюд (продуктов питания) достаточно разнообразен и устраивает меня</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[21][4]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[21][4]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Питание в образовательной организации доступно мне по ценам</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[21][5]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[21][5]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title group\">Оцените соблюдение гигиенических норм в Вашей образовательной организации:</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">В туалетах образовательной организации всегда есть мыло, туалетная бумага и т.д.</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[22][1]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[22][1]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Туалеты образовательной организации имеют все необходимое оборудования</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[22][2]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[22][2]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Каждый корпус образовательной организации оснащен достаточным числом туалетов</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[22][3]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[22][3]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">В туалетах образовательной организации всегда есть горячая вода</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[22][4]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[22][4]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Туалеты образовательной организации поддерживаются в чистоте</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[22][5]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[22][5]\"><span class=\"middle\">Нет</span></label></div>";

    String f = "<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title\">Оцените качество инфраструктуры для занятий спортом и физической культурой в Вашей образовательной организации</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[23]\"><span class=\"middle\">Инфраструктура для занятий физической культурой и спортом отсутствует или практически отсутствует</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[23]\"><span class=\"middle\">Инфраструктура для занятий физической культурой и спортом на минимальном уровне и достаточна только для проведения занятий по физкультуре</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"3\" data-send=\"\" type=\"radio\" name=\"qs[23]\"><span class=\"middle\">Инфраструктура для занятий физической культурой и спортом на среднем уровне</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"4\" data-send=\"\" type=\"radio\" name=\"qs[23]\"><span class=\"middle\">Инфраструктура для занятий физической культурой и спортом на хорошем уровне. Ее достаточно для проведения занятий по физкультуре, а также есть возможность дополнительных занятий спортом</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"5\" data-send=\"\" type=\"radio\" name=\"qs[23]\"><span class=\"middle\">Инфраструктура для занятий физической культурой и спортом достаточна для любых видов спортивной и физической активности</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title group\">Оцените качество инфраструктуры для занятий творчеством и проведения культурно-массовых мероприятий в Вашей образовательной организации:</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">В образовательной организации есть возможности для занятия творчеством</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[24][1]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[24][1]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Состояние актового зала меня удовлетворяет</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[24][2]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[24][2]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Актовый зал хорошо оборудован для проведения культурно-массовых мероприятий</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[24][3]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[24][3]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">В образовательной организации есть помещения для занятий творчеством: секции, клубы, студии и т.д.</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[24][4]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[24][4]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Студенты имеют нормальный доступ к помещениям для занятий творчеством</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[24][5]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[24][5]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title group\">Оцените доступ к сети Интернет:</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">В образовательной организации в учебных корпусах есть точки доступа wi-fi с открытым доступом</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[25][1]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[25][1]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">В образовательной организации в общежитии есть точки доступа wi-fi с открытым доступом</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[25][2]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[25][2]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">В библиотеке образовательной организации имеются компьютеры с доступом в интернет</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[25][3]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[25][3]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">В библиотеке образовательной организации есть доступ к интернет-базам научных ресурсов</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[25][4]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[25][4]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Скорость доступа в интернет меня удовлетворяет</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[25][5]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[25][5]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title group\">Оцените оснащенность Вашей образовательной организации компьютерно-вычислительной техникой и необходимым мультимедийным оборудованием:</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">В образовательной организации достаточно компьютеров для организации учебного процесса</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[26][1]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[26][1]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">На занятиях регулярно используется мультимедийное оборудование</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[26][2]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[26][2]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Мы имеем возможность использовать компьютерную технику для организации учебного процесса в достаточном объеме времени</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[26][3]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[26][3]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Используемая компьютерная техника отвечает всем современным требованиям</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[26][4]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[26][4]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Используемое мультимедийное оснащение отвечает всем современным требованиям</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[26][5]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[26][5]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title group\">Оцените качество инфраструктуры общежития в образовательной организации:</div>";


    String e = "<div class=\"stitle\">Техническое состояние общежития (состояние здания) меня удовлетворяет</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[27][1]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[27][1]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Безопасность общежития на должном уровне (в том числе общежитие защищено от доступа посторонних, соблюдаются правила пожарной безопасности и т.д.)</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[27][2]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[27][2]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">В общежитии соблюдаются все санитарные нормы (освещенность, температура, состояния санузлов и т.д.)</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[27][3]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[27][3]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Общежитие круглосуточно доступно для входа студентов</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[27][4]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[27][4]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Состояние комнат меня удовлетворяет</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[27][5]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[27][5]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title group\">Оцените состояние учебных корпусов образовательной организации:</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Корпус оснащен гардеробом</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[28][1]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[28][1]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Организована система безопасности корпуса</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[28][2]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[28][2]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">В корпусе есть пространство для ожидания обучающихся в перерывах между занятиями</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[28][3]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[28][3]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Корпус поддерживается в чистоте и порядке</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[28][4]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[28][4]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Техническое состояние корпуса меня удовлетворяет</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[28][5]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[28][5]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"question\">\n" +
            "\t\t\t\t\t\t<div class=\"title group\">Оцените удобство и состояние административных помещений образовательной организации:</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Приемные/деканаты оснащены стульями и столами для заполнения заявлений и иных документов</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[29][1]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[29][1]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Оборудованы зоны ожидания для студентов</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[29][2]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[29][2]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Информационные стенды всегда содержат актуальную информацию</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[29][3]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[29][3]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Кассы образовательной организации имеются в достаточном количестве</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[29][4]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[29][4]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"stitle\">Административные помещения организованы так, чтобы избежать очередей</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"1\" data-send=\"\" type=\"radio\" name=\"qs[29][5]\"><span class=\"middle\">Да</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"atitle\"><label class=\"def-radio\"><input value=\"2\" data-send=\"\" type=\"radio\" name=\"qs[29][5]\"><span class=\"middle\">Нет</span></label></div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t<hr class=\"ignore-line hr-margin\">\n" +
            "\t\t\t\t\t\t\t\t\t</div>\n" +
            "\t\t<button class=\"def-button radius4 blue\" id=\"ajax\" data-params=\"_url:/student/ajax/set_votes;_aFunc:success\">Сохранить</button>\n" +
            "\t</div>";






}