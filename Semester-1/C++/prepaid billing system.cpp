#include <iostream>
#include <iomanip>
using namespace std;

//Summary Report Function
void displaySummary(float totalCharge[], int month)
{
    float lowest = totalCharge[0];
    float highest = totalCharge[0];
    float sum = 0;
    float avg;

    for(int i = 0; i < month; i++)
    {
        if(totalCharge[i] > highest)
        {
            highest = totalCharge[i];
        }
        else if(totalCharge[i] < lowest)
        {
            lowest = totalCharge[i];
        }

        sum = sum + totalCharge[i];
    }

    avg = sum / month;

    cout<<fixed<<setprecision(2);
    cout<<"\n====== SUMMARY REPORT ======"<<endl;
    cout<<"Highest Monthly Bill : RM"<<highest<<endl;
    cout<<"Lowest Monthly Bill  : RM"<<lowest<<endl;
    cout<<"Average Monthly Bill : RM"<<avg<<endl;
}

// Utility Calculation Function
void calcUtility(int month, float elec[], float water[], float totalCharge[], float &credit, bool membership)
{
    float elecCharge, waterCharge, total, discount, remaining;
    char cont;

    for(int i = 0; i < month; i++)
    {
        elecCharge = elec[i] * 0.45;
        waterCharge = water[i] *0.02;
        total = elecCharge + waterCharge;

        if(membership == true)
        {
            discount = total * 0.15;
            total = total - discount;
        }
        else
        {
            discount = 0;
        }

        totalCharge[i] = total;

        credit = credit - total;
        remaining = credit;

        cout<<fixed<<setprecision(2);
        cout<<"\nMonth " <<i+1<<endl;
        cout<<"Electricity Charge : RM"<<elecCharge<<endl;
        cout<<"Water Charge       : RM"<<waterCharge<<endl;
        cout<<"Discount           : RM"<<discount<<endl;
        cout<<"Total Charge       : RM"<<total<<endl;
        cout<<"Remaining Credit   : RM"<<remaining<<endl;

        if(remaining <= 10 && remaining >= 0)
        {
            cout<<"WARNING: Credit balance is low!"<<endl;
        }
        else if(remaining < 0)
        {
            cout<<"WARNING: Insufficient credit"<<endl;
            cout<<"Would you like to top up? [Y/N]: ";
            cin>>cont;

            if(cont == 'Y' || cont == 'y')
            {
                float topup;
                cout<<"Enter top-up amount : RM";
                cin>>topup;
                credit = credit + topup;
                cout<<"New Credit Balance : RM"<<credit<<endl;
            }
        }
    }
}

// Main Structure
int main()
{
    int month;
    float credit;
    float elec[20], water[20], totalCharge[20];
    char choice;
    bool membership = false;

    cout<<"\n=============================";
    cout<<"\n   Prepaid Billing System";
    cout<<"\n=============================\n";

    cout<<"\nEnter initial prepaid credit: RM";
    cin>>credit;

    cout<<"Do you have membership? [Y/N]: ";
    cin>>choice;

    if(choice == 'N' || choice == 'n')
    {
        cout<<"Do you want to apply a membership? [Y/N]: ";
        cin>>choice;

        if(choice == 'Y' || choice == 'y')
        {
            credit = credit - 20;
            membership = true;
            cout<<"RM20 will be charge to your credit"<<endl;
            cout<<"15% discount will be applied to all your bill\n";
        }
    }
    else if(choice == 'Y' || choice == 'y')
    {
        membership = true;
    }
    else 
    {
        cout<<"\nInvalid choice.";
        membership = false;
    }

    cout<<"\nEnter number of month: ";
    cin>>month;

    for(int i = 0; i < month; i++)
    {
        cout<<"\n=== Month "<<i+1<<" ===";
        cout<<"\nEnter electricity usage (kWh): ";
        cin>>elec[i];
        cout<<"Enter water usage (litres): ";
        cin>>water[i];
    }

    calcUtility(month, elec, water, totalCharge, credit, membership);

    displaySummary(totalCharge, month);

    return 0;
}
