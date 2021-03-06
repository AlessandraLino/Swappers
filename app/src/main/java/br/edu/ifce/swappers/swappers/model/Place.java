package br.edu.ifce.swappers.swappers.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bolsista on 18/07/2015.
 */
public class Place implements Serializable, Comparable<Place>{
    private int idPlace;
    private String namePlace;
    private String adressPlace;
    
    private int id;
    private String name;
    private String city;
    private String states;
    private String street;
    private String district;
    private String number;
    private String cep;
    private int recovered;
    private int donation;
    private String hour_func;
    private byte[] photo;
    private String photo2;
    private double latitude;
    private double longitude;
    private double distance;
    private List<Book> books = new ArrayList<Book>();

    public Place(){

    }

    public Place(int idPlace, String namePlace, String adressPlace, double latitude, double longitude, double distance) {
        this.idPlace = idPlace;
        this.namePlace = namePlace;
        this.adressPlace = adressPlace;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    public Place(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.namePlace = "Place Next ";
    }

    public Place(double latitude, double longitude, double distance){
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setDistance(distance);
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getDonation() {
        return donation;
    }

    public void setDonation(int donation) {
        this.donation = donation;
    }

    public String getHour_func() {
        return hour_func;
    }

    public void setHour_func(String hour_func) {
        this.hour_func = hour_func;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getAdressPlace() {
        return adressPlace;
    }

    public void setAdressPlace(String adressPlace) {
        this.adressPlace = adressPlace;
    }

    public int getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(int idPlace) {
        this.idPlace = idPlace;
    }

    public String getNamePlace() {
        return namePlace;
    }

    public void setNamePlace(String namePlace) {
        this.namePlace = namePlace;
    }

    @Override
    public int compareTo(Place place){
        if (this.getDistance() < place.getDistance()) {
            return -1;
        }
        if (this.getDistance() > place.getDistance()) {
            return 1;
        }
        return 0;
    }
}
