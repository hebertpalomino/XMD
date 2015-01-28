//
//  ExpensesTableViewController.m
//  accountably
//
//  Created by Steven Palomino on 1/12/15.
//  Copyright (c) 2015 Steven Palomino. All rights reserved.
//

#import "ExpensesTableViewController.h"
#import "ExpensesTableViewCell.h"
#import "AddExpenseViewController.h"
#import <Parse/Parse.h>
#import "Expense.h"
#import "UpdateViewController.h"

@interface ExpensesTableViewController ()
@property (nonatomic, strong) NSArray *test;
@property (nonatomic, strong) NSMutableArray *objects;
@property (nonatomic, strong) UIRefreshControl *refreshControl;
@end

@implementation ExpensesTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.myTableView.delegate = self;

    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(getData)
                                                 name:@"savedExpense"
                                               object:nil];
    
    self.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc]initWithTitle:@"Logout" style:UIBarButtonItemStylePlain target:self action:@selector(logoutUser)];
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(addExpense)];
    [self getData];
    self.objects = [[NSMutableArray alloc]init];
    self.myTableView.allowsMultipleSelectionDuringEditing = NO;
    
    // Pull to refresh
    self.refreshControl = [[UIRefreshControl alloc] init];
    [self.myTableView addSubview:self.refreshControl];
    self.refreshControl.tintColor = [UIColor grayColor];
    [self.refreshControl addTarget:self
                            action:@selector(getData)
                  forControlEvents:UIControlEventValueChanged];

    

}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        //add code here for when you hit delete
        //delete from Parse
        PFQuery *query = [PFQuery queryWithClassName:@"expense"];
        [query whereKey:@"user" equalTo:[PFUser currentUser]];
        [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
            if (!error) {
                
                for (int i = 0; i < self.objects.count; i++){

                    if ([[[objects objectAtIndex:i]valueForKey:@"expenseName"] isEqualToString:[[self.objects objectAtIndex:indexPath.row] name]]){
                        [[objects objectAtIndex:indexPath.row]deleteInBackground];
                        [self.objects removeObjectAtIndex:indexPath.row];
                        [self getData];
                        [self.myTableView reloadData];
                         
                    }
                }
            } else {
                
            }
            
        }];
    }
}


-(void)getData
{
    [self.refreshControl endRefreshing];
    PFQuery *query = [PFQuery queryWithClassName:@"Expense"];
    [query whereKey:@"user" equalTo:[PFUser currentUser]];
    
    [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
        if (!error) {
            // The find succeeded.
            float totalAmount = 0.0f;
            [self.objects removeAllObjects];
            for (int i = 0; i < objects.count; i++ ){
                Expense *exp = [[Expense alloc]init];
                exp.name = [[objects objectAtIndex:i]valueForKey:@"expenseName"];
                exp.amount = [[objects objectAtIndex:i]valueForKey:@"expenseAmount"];
                
                [self.objects addObject:exp];
                totalAmount += exp.amount.floatValue;
            }
            self.totalAmount.text = [NSString stringWithFormat:@"$%.2f", totalAmount];
            [self.myTableView reloadData];
        } else {
            // Failure
            NSLog(@"Error: %@ %@", error, [error userInfo]);
        }
    }];
}

-(void)addExpense
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    AddExpenseViewController *add = (AddExpenseViewController*)[storyboard instantiateViewControllerWithIdentifier:@"addExpense"];
    [self presentViewController:add animated:YES completion:nil];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)logoutUser
{
    NSLog(@"success");
    [PFUser logOut];
    [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"isLoggedIn"];
    [[NSUserDefaults standardUserDefaults]synchronize];
    [self dismissViewControllerAnimated:YES completion:nil];
}


#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
    return self.objects.count;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    ExpensesTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"Cell"];

    cell.expenseTitle.text = [[self.objects objectAtIndex:indexPath.row] name];
    cell.expenseAmount.text = [NSString stringWithFormat:@"$%.2f",[[[self.objects objectAtIndex:indexPath.row] amount] floatValue]];

    
    return cell;
}

#pragma mark Table view selection

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}


#pragma mark - prepareForSegue

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    
    if ([segue.identifier isEqualToString:@"updateObject"]) {
        NSIndexPath *path = [self.myTableView indexPathForSelectedRow];
        Expense *currentExp = [self.objects objectAtIndex:path.row];
        [segue.destinationViewController setCurrentExpense:currentExp];
    }
}










@end
