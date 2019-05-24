package heroesapi;

import java.util.List;
import java.util.Map;

import model.Heroes;
import model.ImageResponse;
import model.LoginSignupResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface HeroesAPI {

    @POST("heroes")
    Call<Void> addHero (@Header("Cookie") String cookie, @Body Heroes heroes);

    @FormUrlEncoded
    @POST("heroes")
    Call<Void> addHero (@Header("Cookie") String cookie,@Field("name") String name,@Field("desc") String desc);


    @FormUrlEncoded
    @POST("heroes")
    Call<Void> addHero (@Header("Cookie") String cookie,@FieldMap Map<String,String> map);

    @Multipart
    @POST("upload")
    Call<ImageResponse> uploadImage(@Part MultipartBody.Part img);

    @GET("heroes")
    Call<List<Heroes>>getAllEmployee(@Header("Cookie") String cookie);


    @FormUrlEncoded
    @POST("users/login")
    Call<LoginSignupResponse> checkUser(@Field("username") String username , @Field("password") String password);

}
