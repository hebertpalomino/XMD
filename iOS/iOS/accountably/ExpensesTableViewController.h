//
//  ExpensesTableViewController.h
//  accountably
//
//  Created by Steven Palomino on 1/12/15.
//  Copyright (c) 2015 Steven Palomino. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ExpensesTableViewController : UIViewController<UITableViewDataSource, UITableViewDelegate>

@property (weak, nonatomic) IBOutlet UITableView *myTableView;
@property (weak, nonatomic) IBOutlet UILabel *totalAmount;

@end
