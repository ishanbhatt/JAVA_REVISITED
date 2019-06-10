package Chapter10;

import java.util.Optional;

public class Car {

    private Insurance insurance;
    /*
    * It is never a good idea to have Optional<Insurance> insurance
    * If that is the case our setter becomes cluttered, and the libraries using it have to be aware that it needs
    * to set Optional<Insurance>, that's horrendous, psvm or other using this either sets the insurance or they don't
    * Also, if they haven't set it, That would mean Optional<Insurance> itself is null
    * Optional attribute evaluating to null! What could be worse
    * So, if you are unsure if it gets set or not, check that in getInsurance using Optional.ofNullable .
    * Calling map on Optional which is null, gives you NPE, but calling map on Optional which can contain null moves it forward
    * */

    public Optional<Insurance> getInsurance() {
        return Optional.ofNullable(insurance);
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }
}
